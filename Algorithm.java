import java.util.*;

import Models.*;

public class Algorithm {
	public List<String> getResult(Map<Terms, Double> oldTermMap, Map<String, Terms> map, int[] resultListSizes, List<String> queryterms){
		double alpha =0.3, beta =0.7, gamma=0.5;
		double max = 0,secondmax = 0;
		String maxword ="", secondword = "";
		//Results of the top 2 relevant words.
		List<String> results = new ArrayList<String>();
		
		//q0=q0*alpha
		for(Terms t : oldTermMap.keySet()){
			double value = oldTermMap.get(t)*alpha;
			oldTermMap.put(t, value);
		}
		
		//Calculate new weight of each term
		for(Terms t : map.values()){
			//Check if the term is already in query
			boolean isInQuery=false;
			for(String qt : queryterms){
				if(t.getValue().equals(qt)){
					isInQuery=true;
					break;
				}
			}
			if(isInQuery) continue;
			
			//weight of term of 10 documents
			double weight=0;
			Map<Documents, Double> Tfidfs=t.getTfidfs();
			for(Documents doc : Tfidfs.keySet()){
				if(doc.getRelevant()){
					weight+=Tfidfs.get(doc)*beta/resultListSizes[0];
				}
				else{
					weight-=Tfidfs.get(doc)*gamma/resultListSizes[1];
				}
			}
			//update term map and pick top two terms
			double final_weight=weight+0.0001*Math.abs(t.getCloseQuery());
			if(oldTermMap.containsKey(t)) final_weight+=oldTermMap.get(t);
			oldTermMap.put(t, final_weight);
			
			//Get the top two terms
			if(final_weight > secondmax){
				if(final_weight > max){
					secondword = maxword;
					secondmax = max;
					maxword = t.getValue();
					max = final_weight;
			
				}else{
					secondword = t.getValue();
					secondmax = final_weight;		
				}		
			}
			
//			System.out.println(t.getValue()+" DF: "+ t.getDf());
//			System.out.println(t.getValue()+" w: "+final_weight);
		}
		
		results.add(secondword);
		results.add(maxword);
		
		//System.out.println(secondmax+" "+max);
		return results;
	}
}
