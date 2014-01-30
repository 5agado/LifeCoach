package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.dao.GoalDao;
import model.dao.MeasureDao;
import model.dao.MeasureDefinitionDao;
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
		c.set(1901, 1, 12);
		p.setBirthdate(c.getTime());
		p = PersonDao.getInstance().create(p);
		assertNotNull("Id should not be null", p.getPersonId());
	}

	@Test
	public void readPerson() {
		Person p = PersonDao.getInstance().read(12);
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
		Person p = PersonDao.getInstance().read(11);
		assertNotNull("The person is not here", p);
		List<MeasureDefinition> def = MeasureDefinitionDao.getInstance()
				.readByName("weight");
		assertNotSame("The measureDef is not here", 0, def.size());
		Measure m = new Measure();
		m.setMeasureDefinition(def.get(0));
		m.setPerson(p);
		Calendar c = Calendar.getInstance();
		c.set(2013, 12, 20);
		m.setTimestamp(c.getTime());
		int value = 79;
		m.setValue(String.valueOf(value));
		MeasureDao.getInstance().create(m);
	}

	@Test
	public void createGoal() {
		// Random r = new Random();
		Person p = PersonDao.getInstance().read(11);
		assertNotNull("The person is not here", p);
		List<MeasureDefinition> def = MeasureDefinitionDao.getInstance()
				.readByName("calories");
		assertNotSame("The measureDef is not here", 0, def.size());
		Goal g = new Goal();
		g.setMeasureDefinition(def.get(0));
		g.setPerson(p);
		g.setComparator("LT");
		g.setDescription(def.get(0).getMeasureName());
		g.setTimestamp(new Date());
		g.setValue("3000");
		Calendar c = Calendar.getInstance();
		// int year = r.nextInt(10) + 2010;
		// int month = r.nextInt(12) + 1;
		// int day = r.nextInt(28) + 1;
		c.set(2013, 4, 20);
		g.setExpDate(c.getTime());
		GoalDao.getInstance().create(g);
	}

	@Test
	public void readMeasureByDate() {
		Person p = PersonDao.getInstance().read(11);
		assertNotNull("The person is not here", p);
		List<MeasureDefinition> profileMeasureDef = MeasureDefinitionDao
				.getInstance().readByName("weight");
		assertNotSame("The measureDef is not here", 0, profileMeasureDef.size());

		Calendar c = Calendar.getInstance();
		c.set(2014, 1, 20);
		Date beforeDate = c.getTime();
		c.set(2014, 3, 20);
		Date afterDate = c.getTime();
		List<Measure> measures = MeasureDao.getInstance().readAllByPersonDefinitionAndDate(
				p.getPersonId(), profileMeasureDef.get(0).getMeasureDefId(),
				beforeDate, afterDate);
		for (Measure m : measures){
			marshalToStdOut(Measure.class, m);
		}
	}

	@Test
	public void createProfileMeasure() {
		Random r = new Random();
		Person p = PersonDao.getInstance().read(11);
		assertNotNull("The person is not here", p);
		List<MeasureDefinition> profileMeasureDef = MeasureDefinitionDao
				.getInstance().readByProfileType("crossfit");
		assertNotSame("The measureDef is not here", 0, profileMeasureDef.size());
		for (MeasureDefinition def : profileMeasureDef) {
			Measure m = new Measure();
			m.setMeasureDefinition(def);
			m.setPerson(p);
			m.setTimestamp(new Date());
			int value = r.nextInt(100) + 1;
			m.setValue(String.valueOf(value));
			MeasureDao.getInstance().create(m);
		}
	}

	@Test
	public void createProfileGoals() {
		Random r = new Random();
		Person p = PersonDao.getInstance().read(11);
		assertNotNull("The person is not here", p);
		List<MeasureDefinition> profileMeasureDef = MeasureDefinitionDao
				.getInstance().readByProfileType("crossfit");
		assertNotSame("The measureDef is not here", 0, profileMeasureDef.size());
		for (MeasureDefinition def : profileMeasureDef) {
			Goal g = new Goal();
			g.setMeasureDefinition(def);
			g.setPerson(p);
			g.setComparator("GT");
			g.setDescription(def.getMeasureName());
			g.setTimestamp(new Date());
			g.setValue("50");
			Calendar c = Calendar.getInstance();
			int year = r.nextInt(10) + 2010;
			int month = r.nextInt(12) + 1;
			int day = r.nextInt(28) + 1;
			c.set(year, month, day);
			g.setExpDate(c.getTime());
			GoalDao.getInstance().create(g);
		}
	}

	@Test
	public void popolateDatabasePerson() {
		final int MAX_PEOPLE = 10;
		Random r = new Random();

		int numPeople = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(
				"src/test/resources/test.csv"))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null
					&& numPeople < MAX_PEOPLE) {
				String[] parts = sCurrentLine.split(",");

				String first = parts[0].substring(1, parts[0].length() - 1);
				String second = parts[1].substring(1, parts[1].length() - 1);
				String mail = parts[parts.length - 2].substring(1,
						parts[parts.length - 2].length() - 1);

				Person person = new Person();
				person.setFirstname(first);
				person.setLastname(second);
				person.setEmail(mail);
				Calendar c = Calendar.getInstance();
				int year = r.nextInt(110) + 1900;
				int month = r.nextInt(12) + 1;
				int day = r.nextInt(28) + 1;
				c.set(year, month, day);
				person.setBirthdate(c.getTime());

				PersonDao.getInstance().create(person);
				numPeople++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void marshalToStdOut(Class<?> objClass, Object obj) {
		try {
			JAXBContext context = JAXBContext.newInstance(objClass);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(obj, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
