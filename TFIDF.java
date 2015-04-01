import java.io.IOException;
import java.util.*;

import Models.BingResult;
import Models.Documents;
import Models.Terms;
import Models.*;

public class TFIDF {
	public void extractQuery(Map<String,Terms> map, List<BingResult> yes, List<BingResult> no, String originQuery) throws IOException{
		if(map==null){
			map=new HashMap<String, Terms>();
		}
		List<Documents> docList = new ArrayList<Documents>();
		StopWords sw = new StopWords();
		
		Documents d = null;
		
		//yesList
		for(BingResult res : yes){
			int max = 0;
			String cur = res.getDesciption().replaceAll("[^A-Z^a-z^\\s]+","").trim().toLowerCase();
			cur = res.getTitle().replaceAll("[^A-Z^a-z^\\s]+","").trim().toLowerCase()+" "+cur;

			String parse[] = cur.split("[\\s]+");
			d = new Documents(res.getUrl(), true);
			
			int position=0;
			for(String s : parse){
				if(!sw.check(s) && !s.equals("")){
					Terms t;
					if(map.containsKey(s)){
						t=map.get(s);
						if(!map.get(s).getYesList().containsKey(d)) map.get(s).AddDf();//If term appear in document d first time, inc df.
						map.get(s).addTime(d, true);
						if(map.get(s).getTimes(d, true) > max){
							max = map.get(s).getTimes(d, true);
						}
					}else{
						t = new Terms(s,true);
						t.addTime(d, true);
						map.put(s,t);
						map.get(s).AddDf();
					}
					d.addPosition(t, position);
					
					//check if the term is close to original term.
					if(position>0&&(!s.equals(originQuery))&&parse[position-1].equals(originQuery)){
						t.addCloseQuery(1);
					}
					else if(position<parse.length-1&&(!s.equals(originQuery))&&parse[position+1].equals(originQuery)){
						t.addCloseQuery(-1);
					}
				}
				position++;
			}
			d.setMax(max);
			docList.add(d);
			max =0;
		}

		//noList
		for(BingResult res : no){
			
			int max = 0;
			String cur = res.getDesciption().replaceAll("[^A-Z^a-z^\\s]+","").trim().toLowerCase();
			cur += " "+res.getTitle().replaceAll("[^A-Z^a-z^\\s]+","").trim().toLowerCase();

			String parse[] = cur.split("[\\s]+");
			d = new Documents(res.getUrl(), false);
			
			int position=0;
			for(String s : parse){
				if(!sw.check(s) && !s.equals("")){
					Terms t;
					if(map.containsKey(s)){
						t=map.get(s);
						if(!map.get(s).getNoList().containsKey(d)) map.get(s).AddDf();
						map.get(s).addTime(d, false);
						if(map.get(s).getTimes(d, false) > max){
							max = map.get(s).getTimes(d, false);
						}
					}else{
						t = new Terms(s,false);
						t.addTime(d, false);
						map.put(s,t);
						map.get(s).AddDf();
					}
					d.addPosition(t, position);
				}
				position++;
			}
			d.setMax(max);
			docList.add(d);
			max =0;
		}
		
		for(Terms t : map.values()){
			t.calcTfidf();
		}
		for(Documents doc : docList){
			doc.getNormalize();
		}
		for(Terms t : map.values()){
			t.calcNormalizedTfidf();
		}
		
/*
		// df
		int df ;
		for(String s : map.keySet()){
			df =0;
			for(int i = 0 ; i < 10; i++){
				if(map.get(s).getTimes()[i]>0){
					df++;
				}
			}
			map.get(s).setDf(df);
		}

		//tf
		for(String s : map.keySet()){
			for(int i = 0 ; i < 10; i++){
				if(map.get(s).getTimes()[i] > 0 && docList.get(i).getMax()!=0){
					if(s.equals(s)){
						
					}
					double tf = 0.5+ 0.5*(double)map.get(s).getTimes()[i]/docList.get(i).getMax();
					
					System.out.println("Times:"+map.get(s).getValue()+": "+map.get(s).getTimes()[i]);
					
					map.get(s).setTfs(i,tf);
				}else{
					map.get(s).setTfs(i, 0.5);
				}
			}
		}

		//weight
		int N = 10;
		for(String s : map.keySet()){
			for(int i = 0 ; i < 10; i++){
				if(map.get(s).getTimes()[i]>0){
					double weight =map.get(s).getTimes()[i]*Math.log(N/map.get(s).getDf()) ;
					docList.get(i).setWeight(weight);
				}
			}
		}*/
	}
}
