package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import model.Measure;
import model.MeasureDefinition;
import model.dao.MeasureDefinitionDao;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLAdapter {
	private Document database = null;
	private XPath xpath = null;
	
	//TODO automatic obtain uri, ask only for sourceName
	public XMLAdapter(String sourceURI) {
		initXmlTools(sourceURI);
	}
	
	public XMLAdapter(InputStream streamSource) {
		initXmlToolsForStream(streamSource);
	}
	
	/*
	 * Loads the XML database and creates xpath object 
	 */
	private void initXmlTools(String sourceName){		
		try{
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true);
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        database = builder.parse(sourceName);
	
	        XPathFactory xFactory = XPathFactory.newInstance();
	        xpath = xFactory.newXPath();
		} catch (SAXException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	    }
    }
	
	private void initXmlToolsForStream(InputStream streamSource){		
		try{
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true);
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        database = builder.parse(streamSource);
	
	        XPathFactory xFactory = XPathFactory.newInstance();
	        xpath = xFactory.newXPath();
		} catch (SAXException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	    }
    }
	
	public Node readNode(String xPathCompileExpression) {
		Node node = null;
		try {
			XPathExpression expr = xpath.compile(xPathCompileExpression);
			node = (Node) expr.evaluate(database, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	public NodeList readNodeList(String xPathCompileExpression) {
		NodeList nodeList = null;
		try {
			XPathExpression expr = xpath.compile(xPathCompileExpression);
			nodeList = (NodeList) expr.evaluate(database, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return nodeList;
	}
	
	public String getAttributeValue(String xPathCompileExpression) {
		try {
			XPathExpression expr = xpath.compile(xPathCompileExpression);
			String attributeValue = expr.evaluate(database, XPathConstants.STRING).toString();
			return attributeValue;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	protected void overwriteCurrentSourceTo(String destinationFilePathname) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(database);
	        StreamResult result = new StreamResult(new File(destinationFilePathname));
	        transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
}
