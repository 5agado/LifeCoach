package lifeCoach.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.Goal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "measureName", "value", "goals",
		"goalsStatusDescription" })
public class LifeCoachMeasure implements Serializable {
	private static final long serialVersionUID = 1L;

	private String value;

	private String measureName;

	@XmlElement(name = "goal")
	private List<Goal> goals;

	@XmlElement(name = "goalStatus")
	private List<String> goalsStatusDescription;

	public LifeCoachMeasure() {
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

	public List<Goal> getGoals() {
		return goals;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	public List<String> getGoalsStatusDescription() {
		return goalsStatusDescription;
	}

	public void setGoalsStatusDescription(List<String> goalsStatusDescription) {
		this.goalsStatusDescription = goalsStatusDescription;
	}
}