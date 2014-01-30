package healthProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import model.Measure;
import model.MeasureDefinition;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.Serializer;
import util.XMLParser;

public class HealthProfileAdapter {
	private static final String INFO_FILES_PATH = "src/main/resources/healthProfile_info_src/";
	private static final String LEVELS_FILENAME = "levels";
	private static final String ADVICE_FILENAME = "advice";
	private static final String PROFILE_FILENAME = "profile";
	private static final String STD_FILE_EXTENSION = ".xml";
	private XMLParser profileAdapter;
	private XMLParser levelsAdapter;
	private XMLParser adviceAdapter;

	public HealthProfileAdapter(String profileType, int personId)
			throws IOException, SAXException, ParserConfigurationException {
		profileAdapter = new XMLParser(INFO_FILES_PATH + profileType + "_"
				+ PROFILE_FILENAME + "_" + personId + STD_FILE_EXTENSION);
		levelsAdapter = new XMLParser(INFO_FILES_PATH + profileType + "_"
				+ LEVELS_FILENAME + STD_FILE_EXTENSION);
		adviceAdapter = new XMLParser(INFO_FILES_PATH + profileType + "_"
				+ ADVICE_FILENAME + STD_FILE_EXTENSION);
	}

	// The measure are without the MeasureDefinition info
	public List<Measure> readMeasures() {
		List<Measure> list = new ArrayList<Measure>();
		NodeList nodeList = profileAdapter.readNodeList("/measures/measure");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			Measure measure = new Measure();
			XMLMeasure xmlMeasure = (XMLMeasure) Serializer.unmarshal(
					XMLMeasure.class, currentNode);
			if (xmlMeasure == null) {
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

	public String readReferenceLevelFor(String measureName) {
		Node node = (Node) levelsAdapter
				.readNode("/ref_levels/level[measureName='" + measureName
						+ "']");
		if (node == null) {
			return "";
		}
		XMLevel level = (XMLevel) Serializer.unmarshal(XMLevel.class, node);
		return level.toString();
	}

	public String readAdviceFor(String measureName) {
		Node node = (Node) adviceAdapter
				.readNode("/advice_list/advice[measureName='" + measureName
						+ "']");
		if (node == null) {
			return "";
		}
		XMLAdvice advice = (XMLAdvice) Serializer.unmarshal(XMLAdvice.class,
				node);
		return advice.getContent();
	}

	// Check if the current loaded XML database is up to date, or if it
	// has been modified. We use the attribute value "updated"
	public boolean isCurrentSourceUpToDate() {
		String attributeValue = profileAdapter
				.getAttributeValue("/measures/@updated");
		return Boolean.valueOf(attributeValue);
	}

	// Set the current loaded XML database as up to date, generally
	// because we have read it and updated the value in remote database
	public void setUpToDate() {
		profileAdapter.setAttributeValue("/measures/@updated",
				String.valueOf(true));
	}

	@XmlRootElement(name = "measure")
	protected static class XMLMeasure {
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
	protected static class XMLevel {
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
	protected static class XMLAdvice {
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
