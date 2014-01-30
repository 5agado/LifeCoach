package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "goalId", "description", "comparator", "value",
		"expDate", "timestamp" })
@Entity
@Table(name = "Goal")
@NamedQueries({
		@NamedQuery(name = "Goal.findAll", query = "SELECT g FROM Goal g"),
		@NamedQuery(name = "Goal.findByPerson", query = "SELECT g FROM Goal g WHERE g.person.personId = :pId"),
		@NamedQuery(name = "Goal.findByPersonAndDefinition", query = "SELECT g FROM Goal g WHERE g.person.personId = :pId "
				+ "AND g.measureDefinition.measureDefId = :mDefId"), })
public class Goal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "comparator")
	private String comparator;

	@Column(name = "description")
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name = "expDate")
	private Date expDate;

	@Id
	@GeneratedValue(generator = "sqlite_goal")
	@TableGenerator(name = "sqlite_goal", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Goal", initialValue = 1, allocationSize = 1)
	@Column(name = "goalId")
	private int goalId;

	@Temporal(TemporalType.DATE)
	@Column(name = "timestamp")
	private Date timestamp;

	@Column(name = "value")
	private String value;

	@XmlTransient
	// bi-directional many-to-one association to MeasureDefinition
	@ManyToOne
	@JoinColumn(name = "measureDefId", referencedColumnName = "measureDefId", insertable = true, updatable = false)
	private MeasureDefinition measureDefinition;

	@XmlTransient
	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "personId", referencedColumnName = "personId", insertable = true, updatable = false)
	private Person person;

	public Goal() {
	}

	public String getComparator() {
		return this.comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getExpDate() {
		return this.expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public int getGoalId() {
		return this.goalId;
	}

	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureDefinition getMeasureDefinition() {
		return this.measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}