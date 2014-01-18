package model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import model.Person;

public class PersonDao extends DaoJpaImpl<Person, Integer>{
	private static final PersonDao INSTANCE = new PersonDao();
	
	public static PersonDao getInstance() {return INSTANCE;}
	
	private PersonDao() {}
	
	public List<Person> readAll() {
		EntityManager entityManager = createEntityManager();
	    List<Person> list = entityManager.createNamedQuery("Person.findAll", Person.class).getResultList();
	    closeConnections(entityManager);
	    return list;
	}
}
