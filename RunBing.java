import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

//Download and add this library to the build path.
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.SAXException;

import Models.BingResult;
import Models.Terms;
import Parser.DOMParserDemo;

public class RunBing {
	double DocumentNo =10.0;
	public int run(String accountKey,String query,double precision,List<String> querylist,boolean first, 
			Map<Terms, Double> oldTermMap, Map<String, Terms> stringMap) throws IOException, ParserConfigurationException, SAXException{
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27"+query+"%27&$top=10&$format=Atom";
//		String accountKey = "kvcaquCfLQMfXkXnNEoUHyoZqIYH2ihIdOGMnXoi3QI";
		
		//Provide your account key here. 
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

		InputStream inputStream = (InputStream) urlConnection.getContent();
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		String content = new String(contentRaw);

		if(first){
			System.out.println("Parameters: ");
			System.out.println("Client Key: "+accountKey);
			System.out.println("Query: "+ query);
			System.out.println("Precision: "+ precision);
			System.out.println("URL: "+ bingUrl);
			System.out.println("Bing Search Results: ");
		}

		//The content string is the xml/json output from Bing.		
		DOMParserDemo d = new DOMParserDemo();
		List<BingResult> resList = d.parser(content);
		
		List<BingResult> yes = new ArrayList<BingResult>();
		List<BingResult> no = new ArrayList<BingResult>();
		Scanner sc = new Scanner(System.in);
		for(int i =0; i< resList.size();i++){
			System.out.println("=======================================\n");
			System.out.println("Results"+(i+1)+": ");
			System.out.println("Title: "+ resList.get(i).getTitle());
			System.out.println("Description: "+ resList.get(i).getDesciption());
			System.out.println("URL: "+ resList.get(i).getUrl()+"\n");
			System.out.println("Relevant (Y/N): ");
			
			while(true){
				String res = sc.next().toString();
				if(res.equals("Y") || res.equals("y")||res.equals("yes")||res.equals("YES")){
					yes.add(resList.get(i));
					break;
				}else if(res.equals("N") || res.equals("n")||res.equals("NO")||res.equals("no")){
					no.add(resList.get(i));
					break;
				}else{
					System.out.println("Please input again(Y/N): ");
				}
			}
			
		}
		if(yes.isEmpty()){
			return -1;
		}else{
			TFIDF tfidf = new TFIDF();
			tfidf.extractQuery(stringMap, yes, no, querylist.get(0));
			
			Algorithm al = new Algorithm ();
			int[] resultListSizes={yes.size(),no.size()};
			List<String> res_term=al.getResult(oldTermMap, stringMap, resultListSizes, querylist);
			for(String r : res_term){
				querylist.add(r);
			}
		}
		int result = yes.size();
		return result;
	}
	
	public String getQueryString(List<String> querylist, Map<String, Terms> termMap){
		String query="";
		int[] closeCounts=new int[querylist.size()];
		for(int i=0;i<querylist.size();i++){
			closeCounts[i]=termMap.get(querylist.get(i)).getCloseQuery();
		}
		//append terms which are likely to appear right before the original query
		for(int i=1;i<querylist.size();i++){
			if(closeCounts[i]<0){
				if(query.equals("")) query+=querylist.get(i);
				else query=query+"%20"+querylist.get(i);
			}
		}
		//append original query term
		if(query.equals("")) query+=querylist.get(0);
		else query=query+"%20"+querylist.get(0);
		//append terms which are likely to appear right after the original query
		for(int i=1;i<querylist.size();i++){
			if(closeCounts[i]>0){
				query=query+"%20"+querylist.get(querylist.size()-i);
			}
		}
		//append other terms
		for(int i=1;i<querylist.size();i++){
			if(closeCounts[i]==0){
				query=query+"%20"+querylist.get(i);
			}
		}
		return query;
	}
}
