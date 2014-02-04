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
@XmlType(propOrder = { "measureId", "value", "timestamp", "measureDefinition" })
@Entity
@Table(name = "Measure")
@NamedQueries({
		@NamedQuery(name = "Measure.findAll", query = "SELECT m FROM Measure m"),
		@NamedQuery(name = "Measure.findByPerson", query = "SELECT m FROM Measure m WHERE m.person.personId = :pId"),
		@NamedQuery(name = "Measure.findByPersonDefinitionAndId", query = "SELECT m FROM Measure m WHERE m.person.personId = :pId "
				+ "AND m.measureId = :mId AND m.measureDefinition.measureDefId = :mDefId"),
		@NamedQuery(name = "Measure.findByPersonAndDefinition", query = "SELECT m FROM Measure m WHERE m.person.personId = :pId "
				+ "AND m.measureDefinition.measureDefId = :mDefId"), })
public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "sqlite_measure")
	@TableGenerator(name = "sqlite_measure", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Measure", initialValue = 1, allocationSize = 1)
	@Column(name = "measureId")
	private int measureId;

	@Temporal(TemporalType.DATE)
	@Column(name = "timestamp")
	private Date timestamp;

	@Column(name = "value")
	private String value;

	// bi-directional many-to-one association to MeasureDefinition
	@ManyToOne
	@JoinColumn(name = "measureDefId", referencedColumnName = "measureDefId", insertable = true, updatable = false)
	private MeasureDefinition measureDefinition;

	@XmlTransient
	// bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name = "personId", referencedColumnName = "personId", insertable = true, updatable = false)
	private Person person;

	public Measure() {
	}

	public int getMeasureId() {
		return this.measureId;
	}

	public void setMeasureId(int measureId) {
		this.measureId = measureId;
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