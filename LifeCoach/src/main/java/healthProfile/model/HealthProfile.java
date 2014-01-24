package healthProfile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType(propOrder = {"timestamp", "measures", "suggestions" })
public class HealthProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "date")
	private Date timestamp;
	
	@XmlElement(name = "measure")
	private List<HealthMeasure> measures;
	
	private String suggestions;
	
	public HealthProfile() {
		measures = new ArrayList<HealthMeasure>();
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public List<HealthMeasure> getMeasures() {
		return measures;
	}

	public void setMeasures(List<HealthMeasure> measure) {
		this.measures = measure;
	}
}
