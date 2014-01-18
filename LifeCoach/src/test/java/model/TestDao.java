package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.dao.PersonDao;

import org.junit.Test;

public class TestDao {
	@Test
	public void createPerson() {
		Person p = new Person();
		p.setPersonId(0);
		p.setFirstname("Ora");
		p.setLastname("Labora");
		Calendar c = Calendar.getInstance();
		c.set(2001, 1, 12);
		p.setBirthdate(c.getTime());
		p = PersonDao.getInstance().create(p);
		assertNotNull("Id should not be null", p.getPersonId());
	}
	
	@Test
	public void readPerson() {
		Person p = PersonDao.getInstance().read(0);
		assertNotNull("The person is not here", p);
		marshalToStdOut(Person.class, p);
	}
	
	@Test
	public void updatePerson() {
		Person p = PersonDao.getInstance().read(0);
		assertNotNull("The person is not here", p);
		p.setFirstname("NewDante");
		p.setLastname("NewAlighieri");
		Calendar c = Calendar.getInstance();
		c.set(1984, 6, 21);
		p.setBirthdate(c.getTime());
		p = PersonDao.getInstance().update(p);
		assertNotNull("Id should not be null", p.getPersonId());
	}
	
	@Test
	public void deletePerson() {
		int pId = 2;
		Person p = PersonDao.getInstance().read(pId);
		assertNotNull("The person is ALREADY not here", p);
		PersonDao.getInstance().delete(p);
		p = PersonDao.getInstance().read(pId);
		assertNull("Id should be null", p);
	}
	
	@Test
	public void createMeasure() {
		
	}
	
	@Test
	public void readMeasure() {
		
	}
	
	@Test
	public void updateMeasure() {
		
	}
	
	@Test
	public void deleteMeasure() {
		
	}
	
	@Test
	public void createMeasureDefinition() {
		
	}
	
	@Test
	public void readMeasureDefinition() {
		
	}
	
	@Test
	public void updateMeasureDefinition() {
		
	}
	
	@Test
	public void deleteMeasureDefinition() {
		
	}
	
	/*
	@Test
	public void saveNewHealthProfileWithDao() {
		Person p = PersonDao.getPersonById(2);
		assertNotNull("The person is not here", p);
		HealthProfile hp = new HealthProfile();
		hp.setPerson(p);
		hp.setCalories(1003l);
		hp.setHeight(14.45);
		hp.setSteps(1200);
		hp.setWeight(18.67);
		Calendar c = Calendar.getInstance();
		c.set(2013, 5, 21);
		hp.setDate(c.getTime());
		HealthProfileDao.saveHealthProfile(hp);
		
		assertNotNull("Id should not be null", hp.getId());
	}
	
	@Test
	public void updateHealthProfileWithDao() {
		Person p = PersonDao.getPersonById(151);
		assertNotNull("The person is not here", p);
		HealthProfile hp = HealthProfileDao.getHealthProfileById(1);
		//case insert
		if (hp == null){
			hp = new HealthProfile();
			hp.setPerson(p);
			hp.setCalories(1000l);
			hp.setHeight(13.45);
			hp.setSteps(3000);
			hp.setWeight(45.67);
			Calendar c = Calendar.getInstance();
			c.set(1884, 5, 21);
			hp.setDate(c.getTime());
			hp = HealthProfileDao.saveHealthProfile(hp);
		}
		//case update
		else {
			hp.setCalories(hp.getCalories() + 1);
			hp.setHeight(13.45);
			hp.setSteps(3000);
			hp.setWeight(45.67);
			Calendar c = Calendar.getInstance();
			c.set(1884, 5, 21);
			hp.setDate(c.getTime());
			hp = HealthProfileDao.updateHealthProfile(hp);
		}
		
		assertNotNull("Id should not be null", hp.getId());
	}
	
	@Test
	public void getAll() {
		List<Person> people = PersonDao.getAll();
		assertNotNull("The list of people should not be null", people);
		assertEquals("getAll Person count", 2, people.size());
	}
	
	@Test
	public void getHealthProfileByIdAndOwner() {
		int personId = 1;
		Person p = PersonDao.getPersonById(personId);
		assertNotNull("The person is not here", p);
		HealthProfile hp = HealthProfileDao.getHealthProfileByIdAndOwner(2, personId);
		assertNotNull("The health profile is not here", hp);
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(HealthProfile.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(hp, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAndOutputHealthProfileHistory() {
		Person p = PersonDao.getPersonById(11);
		assertNotNull("The person is not here", p);
		List<HealthProfile> history = p.getHealthProfileHistory();
		HealthProfileHistory hpHistory = new HealthProfileHistory();
		hpHistory.setHealthProfiles(history);
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(HealthProfileHistory.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(hpHistory, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}*/
	
	public static void marshalToStdOut(Class<?> objClass, Object obj){
		try {
			JAXBContext context = JAXBContext.newInstance(objClass);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(obj, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
