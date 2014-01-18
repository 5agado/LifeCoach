package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-12-21T19:13:20.652+0100")
@StaticMetamodel(Measure.class)
public class Measure_ {
	public static volatile SingularAttribute<Measure, Integer> measureId;
	public static volatile SingularAttribute<Measure, String> value;
	public static volatile SingularAttribute<Measure, MeasureDefinition> measureDefinition;
	public static volatile SingularAttribute<Measure, Person> person;
}
