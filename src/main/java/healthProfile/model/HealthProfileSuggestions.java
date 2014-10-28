package healthProfile.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "numMeasures", "numDetailedMeasures", "lowW", "mediumW",
		"highW", "illnessLevel", "advice" })
public class HealthProfileSuggestions implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "allMeasure_number")
	int numMeasures;

	@XmlElement(name = "detailedMeasure_number")
	int numDetailedMeasures;

	@XmlElement(name = "low_warnings")
	int lowW;

	@XmlElement(name = "medium_warnings")
	int mediumW;

	@XmlElement(name = "high_warnings")
	int highW;

	@XmlElement(name = "overall_illness_level")
	String illnessLevel;

	@XmlElement(name = "advice")
	List<String> advice;

	public HealthProfileSuggestions() {
	}

	public int getNumMeasures() {
		return numMeasures;
	}

	public void setNumMeasures(int numMeasures) {
		this.numMeasures = numMeasures;
	}

	public int getNumDetailedMeasures() {
		return numDetailedMeasures;
	}

	public void setNumDetailedMeasures(int numDetailedMeasures) {
		this.numDetailedMeasures = numDetailedMeasures;
	}

	public int getLowW() {
		return lowW;
	}

	public void setLowW(int lowW) {
		this.lowW = lowW;
	}

	public int getMediumW() {
		return mediumW;
	}

	public void setMediumW(int mediumW) {
		this.mediumW = mediumW;
	}

	public int getHighW() {
		return highW;
	}

	public void setHighW(int highW) {
		this.highW = highW;
	}

	public String getIllnessLevel() {
		return illnessLevel;
	}

	public void setIllnessLevel(String illnessLevel) {
		this.illnessLevel = illnessLevel;
	}

	public List<String> getAdvice() {
		return advice;
	}

	public void setAdvice(List<String> advice) {
		this.advice = advice;
	}
}
