package test;

import java.io.File;
import java.io.IOException;

import ja3d.loaders.ParserCollada;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Main {
	
	public static void main(String[] args) 
			throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new File("src/test/xiangzi.xml"));
		
		ParserCollada p = new ParserCollada();
		p.parse(document, "", false);
		
	}

}
