package Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Models.BingResult;

public class DOMParserDemo {
	public List<BingResult> parser(String content) throws ParserConfigurationException, SAXException, IOException{

		InputSource source = new InputSource(new StringReader(content));

		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();

		//Get the DOM Builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(source);

		//Iterating through the nodes and extracting the data.
		NodeList nodeList = document.getDocumentElement().getChildNodes();

		BingResult res = null;

		List<BingResult> resList = new ArrayList<BingResult>();

		String title = null, url = null , description=null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node instanceof Element) {
				NodeList childNodes = node.getChildNodes();

				for (int j = 0; j < childNodes.getLength(); j++) {

					Node cNode = childNodes.item(j);

					if (cNode instanceof Element) {
						NodeList ccNodes = cNode.getChildNodes();

						for(int q=0; q < ccNodes.getLength(); q++){
							Node ccNode = ccNodes.item(q);
							if(ccNode.getNodeName() == "m:properties"){
								NodeList ccnodelist = ccNode.getChildNodes();
								for(int z =0; z < ccnodelist.getLength();z++){
									Node c3node = ccnodelist.item(z);
									if(c3node instanceof Element){
										if(c3node.getNodeName() == "d:Title"){
											title = c3node.getTextContent().trim();
										}
										if(c3node.getNodeName() == "d:Description"){
											description = c3node.getTextContent().trim();
										}
										if(c3node.getNodeName() == "d:Url"){
											url = c3node.getTextContent().trim();
										}
										res = new BingResult(title,description,url);
									}
								}
								resList.add(res);
							}
						}
					}
				}
			}
		}
		return resList;
	}
}