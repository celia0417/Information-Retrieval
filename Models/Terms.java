package Models;

import java.util.HashMap;
import java.util.Map;

public class Terms {
	String value;//string of the term
	boolean status;
	//store the term frequency in documents
	Map<Documents, Integer> yeslist;
	Map<Documents, Integer> nolist;
	
	Map<Documents, Double> Tfidfs;
	int df;
	
	//If the word is likely to be close to the original query word
	int closeToOriginQuery;
	
	public Terms(String v,boolean status){
		this.setValue(v);
		this.setStatus(status);
		yeslist=new HashMap<Documents, Integer>();
		nolist=new HashMap<Documents, Integer>();
		Tfidfs=new HashMap<Documents, Double>();
		this.df=0;
		closeToOriginQuery=0;
 	}
	
	public Map<Documents, Integer> getYesList(){
		return yeslist;
	}
	public Map<Documents, Integer> getNoList(){
		return nolist;
	}
	public void setStatus(boolean s){
		this.status = s;
	}
	public boolean getStatus(){
		return this.status;
	}
	public void setValue(String value){
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}
	
	public void addTime(Documents i, boolean yesno){
		if(yesno){
			int oldtime=0;
			if(yeslist.containsKey(i)) oldtime+=yeslist.get(i);
			yeslist.put(i, oldtime+1);
		}
		else{
			int oldtime=0;
			if(nolist.containsKey(i)) oldtime+=nolist.get(i);
			nolist.put(i, oldtime+1);
		}
	}
	public void setTimes(Documents i,int time, boolean yesno){
		if(yesno) yeslist.put(i, time);
		else nolist.put(i,  time);
	}
	public int getTimes (Documents i, boolean yesno){
		if(yesno){
			if(yeslist.containsKey(i)) return yeslist.get(i);
			return 0;
		}
		else{
			if(nolist.containsKey(i)) return nolist.get(i);
			return 0;
		}
	}
//	public void setTfs(int i, double tf){
//		this.tfs[i] = tf;
//	}
//	public double [] getTfs(){
//		return this.tfs;
//	}
	public void AddDf(){
		df++;
	}
	public void setDf(int d){
		this.df = d;
	}
	public int getDf(){
		return this.df;
	}
	
	public void calcTfidf(){
		double idf=Math.log(10.0/df);
		for(Documents doc:yeslist.keySet()){
			int tf=yeslist.get(doc);
			double logtf=0;
			if(tf>0){
				logtf=1+Math.log((double)tf);
			}
			double tfidf=logtf*idf;
			Tfidfs.put(doc, tfidf);
		}
		for(Documents doc:nolist.keySet()){
			int tf=nolist.get(doc);
			double logtf=0;
			if(tf>0){
				logtf=1+Math.log((double)tf);
			}
			double tfidf=logtf*idf;
			Tfidfs.put(doc, tfidf);
		}
	}
	
	public void calcNormalizedTfidf(){
		for(Documents doc:yeslist.keySet()){
			double tfidf=Tfidfs.containsKey(doc)?Tfidfs.get(doc):0;
			double norm=doc.normalizedWeight;
			Tfidfs.put(doc, tfidf*norm);
		}
		for(Documents doc:nolist.keySet()){
			double tfidf=Tfidfs.containsKey(doc)?Tfidfs.get(doc):0;
			double norm=doc.normalizedWeight;
			Tfidfs.put(doc, tfidf*norm);
		}
	}
	public Map<Documents, Double> getTfidfs(){
		return Tfidfs;
	}
	
	public void addCloseQuery(int k){
		closeToOriginQuery+=k;
	}
	public int getCloseQuery(){
		return closeToOriginQuery;
	}
}
