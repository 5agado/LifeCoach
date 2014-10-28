package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-01-25T19:43:27.413+0100")
@StaticMetamodel(MeasureDefinition.class)
public class MeasureDefinition_ {
	public static volatile SingularAttribute<MeasureDefinition, Integer> measureDefId;
	public static volatile SingularAttribute<MeasureDefinition, String> measureName;
	public static volatile SingularAttribute<MeasureDefinition, String> measureType;
	public static volatile SingularAttribute<MeasureDefinition, String> profileType;
	public static volatile ListAttribute<MeasureDefinition, Measure> measures;
}
