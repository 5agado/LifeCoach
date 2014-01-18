package lifeCoach;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.Goal;
import model.Measure;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType(propOrder = {"measure", "goal", "suggestions", "motivational" })
public class LifeCoachReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "measure")
	private List<Measure> measures;
	
	@XmlElement(name = "goal")
	private List<Goal> goals;
	
	private String suggestions;
	
	private String motivational;
	
	public LifeCoachReport() {
		// TODO Auto-generated constructor stub
	}

	public List<Measure> getMeasures() {
		return measures;
	}

	public void setMeasures(List<Measure> measures) {
		this.measures = measures;
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public String getMotivational() {
		return motivational;
	}

	public void setMotivational(String motivational) {
		this.motivational = motivational;
	}
}
