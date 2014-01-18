package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.ProfileMeasure.Measure;

@Generated(value="Dali", date="2014-01-08T18:30:05.970+0100")
public class ProfileMeasure_ {
	@StaticMetamodel(Measure.class)
	public static class Measure_ {
		public static volatile SingularAttribute<Measure, Integer> measureId;
		public static volatile SingularAttribute<Measure, Date> timestamp;
		public static volatile SingularAttribute<Measure, String> value;
		public static volatile SingularAttribute<Measure, MeasureDefinition> measureDefinition;
		public static volatile SingularAttribute<Measure, Person> person;
	}
}
