import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class StopWords {
	HashSet<String> set = null;
	
	public boolean check(String word) throws IOException{
		set = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));
		String line = br.readLine();
		while(line != null){
			set.add(line.toString());
			line = br.readLine();
		}
		br.close();
		word = word.toLowerCase();
		if(set.contains(word)){
			return true;
		}else{
			return false;
		}
	}
	public static void main(String args[]) throws IOException{
		StopWords s = new StopWords();
		String ss = "encyclopedia";
		System.out.println(s.check(ss));
	}
}
