package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-01-25T19:43:27.413+0100")
@StaticMetamodel(Measure.class)
public class Measure_ {
	public static volatile SingularAttribute<Measure, Integer> measureId;
	public static volatile SingularAttribute<Measure, Date> timestamp;
	public static volatile SingularAttribute<Measure, String> value;
	public static volatile SingularAttribute<Measure, MeasureDefinition> measureDefinition;
	public static volatile SingularAttribute<Measure, Person> person;
}
