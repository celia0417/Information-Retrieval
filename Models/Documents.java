package Models;
import java.util.*;

public class Documents {
	List<Double> weight = new ArrayList<Double>();  // w = tf * log (N/df)
	Map<Terms, List<Integer>> termPositions=new HashMap<Terms, List<Integer>>();
	int max;
	String url = new String ();
	double normalizedWeight=0;
	boolean relevant;
	
	public void addPosition(Terms term, int pos){
		if(!termPositions.containsKey(term)){
			List<Integer> list=new ArrayList<Integer>();
			list.add(pos);
			termPositions.put(term, list);
		}
		else{
			termPositions.get(term).add(pos);
		}
	}
	public Documents(String url, boolean rel){
		this.url=url;
		relevant=rel;
	}
	public boolean getRelevant(){
		return relevant;
	}
	public void setMax(int max){
		this.max=max;
	}
	public int getMax(){
		return this.max;
	}
	public void setWeight(double w){
		this.weight.add(w);
	}
	public List<Double> getWeight(){
		return this.weight;
	}
	public void getNormalize(){
		double norm=0;
		for(Terms t:termPositions.keySet()){
			double tfidf=t.Tfidfs.get(this);
			norm+=tfidf*tfidf;
		}
		norm=Math.sqrt(norm);
		if(norm!=0){
			normalizedWeight=1.0/norm;
		}
	}
}
