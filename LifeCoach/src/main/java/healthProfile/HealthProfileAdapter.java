package healthProfile;

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

import util.Serializer;
import util.XMLAdapter;

public class HealthProfileAdapter extends XMLAdapter {	
	private String sourceName;
	
	//TODO automatic obtain uri, ask only for sourceName
	public HealthProfileAdapter(String sourceURI) {
		super(sourceURI);
		sourceName = sourceURI;
	}
	
	//The measure are without the MeasureDefinition info
	public List<Measure> readMeasures() {
		List<Measure> list = new ArrayList<Measure>();
		NodeList nodeList = readNodeList("/measures/measure");
        
		for (int i=0; i<nodeList.getLength(); i++){
			Node currentNode = nodeList.item(i);	
			Measure measure = new Measure();
			XMLMeasure xmlMeasure = (XMLMeasure) Serializer.unmarshal(XMLMeasure.class, currentNode);
			if (xmlMeasure == null){
				continue;
			}
			MeasureDefinition def = new MeasureDefinition();
			def.setMeasureName(xmlMeasure.getMeasureName());
			measure.setMeasureDefinition(def);
			measure.setMeasureId(0);
			measure.setValue(xmlMeasure.getValue());
			list.add(measure);
		}
        return list;
	}
	
	public XMLevel readReferenceLevelFor(String measureName) {
		Node node = (Node) readNode("/ref_levels/level[measureName='" + measureName + "']");
		XMLevel level = (XMLevel) Serializer.unmarshal(XMLevel.class, node);
        return level;
	}
	
	public String readAdviceFor(String measureName) {
		Node node = (Node) readNode("/advice_list/advice[measureName='" + measureName + "']");
		XMLAdvice advice = (XMLAdvice) Serializer.unmarshal(XMLAdvice.class, node);
        return advice.getContent();
	}
	
	public String readReferenceLevelAsString(String measureName) {
		XMLevel level = readReferenceLevelFor(measureName);
		return level.toString();
	}
	
	//Check if the current loaded XML database is up to date, or if it
	//has been modified. We use the attribute value "updated"
	public boolean isCurrentSourceUpToDate() {
		String attributeValue = getAttributeValue("/measures/@updated");
		return Boolean.valueOf(attributeValue);
	}
	
	//Set the current loaded XML database as up to date, generally
	//because we have read it and updated the value in local database 
	public void setUpToDate() {
		Node node = (Node) readNode("/measures/@updated");
		node.setNodeValue(String.valueOf(true));
		
		overwriteCurrentSourceTo(sourceName);
	}
	
	@XmlRootElement(name = "measure")
	private static class XMLMeasure {
		private String measureName;
		private String value;
		
		public String getMeasureName() {
			return measureName;
		}
		public void setMeasureName(String measureName) {
			this.measureName = measureName;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	@XmlRootElement(name = "level")
	private static class XMLevel {
		private String measureName;
		private String min;
		private String max;
		
		public String getMeasureName() {
			return measureName;
		}
		public void setMeasureName(String measureName) {
			this.measureName = measureName;
		}
		public String getMin() {
			return min;
		}
		public void setMin(String min) {
			this.min = min;
		}
		public String getMax() {
			return max;
		}
		public void setMax(String max) {
			this.max = max;
		}	
		
		@Override
		public String toString() {
			return min + " - " + max;
		}
	}
	
	@XmlRootElement(name = "advice")
	private static class XMLAdvice {
		private String measureName;
		private String content;
		
		public String getMeasureName() {
			return measureName;
		}
		public void setMeasureName(String measureName) {
			this.measureName = measureName;
		}
		
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
}
