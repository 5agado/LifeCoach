package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-12-21T19:11:33.026+0100")
@StaticMetamodel(Person.class)
public class Person_ {
	public static volatile SingularAttribute<Person, Date> birthdate;
	public static volatile SingularAttribute<Person, String> email;
	public static volatile SingularAttribute<Person, String> firstname;
	public static volatile SingularAttribute<Person, String> lastname;
	public static volatile SingularAttribute<Person, Integer> personId;
	public static volatile ListAttribute<Person, Measure> measures;
}
