package healthProfile.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"measureName", "value", "refLevel", "warning" })
public class HealthMeasure implements Serializable {
	private static final long serialVersionUID = 1L;

	private String value;

	private String measureName;

	private String refLevel;
	
	private String warning;

	public HealthMeasure() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getRefLevel() {
		return refLevel;
	}

	public void setRefLevel(String refLevel) {
		this.refLevel = refLevel;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}
}