import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Models.BingResult;
import Models.Terms;


public class MainTest {
	
	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException{
		RunBing rb = new RunBing();
		//input precision, bing account, query
		String accountKey = args[0];
		double precision = Double.parseDouble(args[1]);
		String query = args[2];
		
		int result = 0;
		boolean first = true;
		List<String> querylist = new ArrayList<String>();
		String[] qs=query.split("%20");
		for(String qterm : qs){
			querylist.add(qterm);
		}
		
		HashMap<Terms, Double> termMap=new HashMap<Terms, Double>();
		while(true){
			Map<String, Terms> stringMap=new HashMap<String, Terms>();
			result = rb.run(accountKey, query, precision, querylist, first, termMap, stringMap);
			if(result==-1){
				System.out.println("Empty search result. \nExiting...");
				break;
			}
			else if(result > (precision*10-0.1)){
				System.out.println("Precision "+(result*1.0/10));
				System.out.println("Desired precision reached, done.");
				break;
			}
			else{
				query=rb.getQueryString(querylist, stringMap);
				first = false;
				
				System.out.println("=====================");
				System.out.println("FEEDBACK SUMMARY");
				System.out.println("Precision "+(result*1.0/10));
				System.out.println("Query "+query.replaceAll("\\%20", " ")+"");
			}
		}
	}
}
