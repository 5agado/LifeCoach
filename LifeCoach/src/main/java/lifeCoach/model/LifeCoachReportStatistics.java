package lifeCoach.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType(propOrder = {"numGoals", "successes", "failures", "stillInprogress"})
public class LifeCoachReportStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "goals_number")
	int numGoals;
	
	@XmlElement(name = "successes")
	int successes;
	
	@XmlElement(name = "failures")
	int failures;
	
	@XmlElement(name = "still_in_progress")
	int stillInprogress;
	
	public LifeCoachReportStatistics() {}

	public int getNumGoals() {
		return numGoals;
	}

	public void setNumGoals(int numGoals) {
		this.numGoals = numGoals;
	}

	public int getSuccesses() {
		return successes;
	}

	public void setSuccesses(int successes) {
		this.successes = successes;
	}

	public int getFailures() {
		return failures;
	}

	public void setFailures(int failures) {
		this.failures = failures;
	}

	public int getStillInprogress() {
		return stillInprogress;
	}

	public void setStillInprogress(int stillInprogress) {
		this.stillInprogress = stillInprogress;
	}
}
