package lifeCoach.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.Person;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType(propOrder = {"timestamp", "person", "measures", "statistics", "motivational" })
public class LifeCoachReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "date")
	private Date timestamp;
	
	private Person person;

	@XmlElement(name = "measure")
	private List<LifeCoachMeasure> measures;
	
	private LifeCoachReportStatistics statistics;

	private String motivational;
	
	public LifeCoachReport() {
	
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<LifeCoachMeasure> getMeasures() {
		return measures;
	}

	public void setMeasures(List<LifeCoachMeasure> measures) {
		this.measures = measures;
	}
	
	public LifeCoachReportStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(LifeCoachReportStatistics statistics) {
		this.statistics = statistics;
	}

	public String getMotivational() {
		return motivational;
	}

	public void setMotivational(String motivational) {
		this.motivational = motivational;
	}
}
