package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	private final static Logger LOGGER = Logger.getLogger(XMLParser.class
			.getName());
	private Document database = null;
	private XPath xpath = null;
	private String sourceUri = null;

	public XMLParser(String sourceURI) throws IOException, SAXException,
			ParserConfigurationException {
		this.sourceUri = sourceURI;
		initXmlTools(sourceURI);
	}

	public XMLParser(InputStream streamSource) throws SAXException,
			IOException, ParserConfigurationException {
		initXmlToolsForStream(streamSource);
	}

	/*
	 * Loads the XML database and creates xpath object
	 */
	private void initXmlTools(String sourceName) throws IOException,
			SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		database = builder.parse(sourceName);

		XPathFactory xFactory = XPathFactory.newInstance();
		xpath = xFactory.newXPath();
	}

	private void initXmlToolsForStream(InputStream streamSource)
			throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		database = builder.parse(streamSource);

		XPathFactory xFactory = XPathFactory.newInstance();
		xpath = xFactory.newXPath();
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
			nodeList = (NodeList) expr.evaluate(database,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return nodeList;
	}

	public String getAttributeValue(String xPathCompileExpression) {
		try {
			XPathExpression expr = xpath.compile(xPathCompileExpression);
			String attributeValue = expr.evaluate(database,
					XPathConstants.STRING).toString();
			return attributeValue;
		} catch (XPathExpressionException e) {
			LOGGER.log(Level.WARNING, "No such Attribute", e.getLocalizedMessage());
			return "";
		}
	}

	public boolean setAttributeValue(String xPathCompileExpression, String value) {
		Node node = (Node) readNode(xPathCompileExpression);
		node.setNodeValue(value);
		boolean res = saveChanges();
		return res;
	}

	private boolean saveChanges() {
		if (sourceUri == null) {
			LOGGER.log(Level.WARNING,
					"This adapter is from stream source. Cannot call saveChanges");
			return false;
		}
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(database);
			StreamResult result = new StreamResult(new File(sourceUri));
			transformer.transform(source, result);
			return true;
		} catch (TransformerException e) {
			LOGGER.log(Level.WARNING, "Stream saving failed", e.getLocalizedMessage());
			return false;
		}
	}
}
