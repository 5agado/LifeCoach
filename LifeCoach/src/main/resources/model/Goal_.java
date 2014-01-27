package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-01-25T19:43:27.413+0100")
@StaticMetamodel(Goal.class)
public class Goal_ {
	public static volatile SingularAttribute<Goal, String> comparator;
	public static volatile SingularAttribute<Goal, String> description;
	public static volatile SingularAttribute<Goal, Date> expDate;
	public static volatile SingularAttribute<Goal, Integer> goalId;
	public static volatile SingularAttribute<Goal, Date> timestamp;
	public static volatile SingularAttribute<Goal, String> value;
	public static volatile SingularAttribute<Goal, MeasureDefinition> measureDefinition;
	public static volatile SingularAttribute<Goal, Person> person;
}
