package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"measureDefId", "measureName", "profileType"})
@Entity
@Table(name="MeasureDefinition")
@NamedQueries({
	@NamedQuery(name="MeasureDefinition.findAll", query="SELECT m FROM MeasureDefinition m"),
	@NamedQuery(name="MeasureDefinition.findByName", query="SELECT m FROM MeasureDefinition m WHERE m.measureName = :measureName"),
	@NamedQuery(name="MeasureDefinition.findByProfileType", query="SELECT m FROM MeasureDefinition m WHERE m.profileType = :profileType")
})
public class MeasureDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_measureDef")
	@TableGenerator(name="sqlite_measureDef", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="MeasureDefinition",
	    initialValue=1, allocationSize=1)
	@Column(name="measureDefId")
	private int measureDefId;

	@Column(name="measureName")
	private String measureName;

	@XmlTransient
	@Column(name="measureType")
	private String measureType;

	@Column(name="profileType")
	private String profileType;

	@XmlTransient
	//bi-directional many-to-one association to Measure
	@OneToMany(mappedBy="measureDefinition")
	private List<Measure> measures;

	public MeasureDefinition() {
	}

	public int getMeasureDefId() {
		return this.measureDefId;
	}

	public void setMeasureDefId(int measureDefId) {
		this.measureDefId = measureDefId;
	}

	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getMeasureType() {
		return this.measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public String getProfileType() {
		return this.profileType;
	}

	public void setProfileType(String profileType) {
		this.profileType = profileType;
	}

	public List<Measure> getMeasures() {
		return this.measures;
	}

	public void setMeasures(List<Measure> measures) {
		this.measures = measures;
	}

	public Measure addMeasure(Measure measure) {
		getMeasures().add(measure);
		measure.setMeasureDefinition(this);

		return measure;
	}

	public Measure removeMeasure(Measure measure) {
		getMeasures().remove(measure);
		measure.setMeasureDefinition(null);

		return measure;
	}

}