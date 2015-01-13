package resources.ws;

import java.util.List;

import javax.jws.WebService;

import model.Person;
import model.ResourceResponse;
import model.ResourceResponse.Status;
import model.dao.PersonDao;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project",
	endpointInterface = "resources.ws.IPersonResource",
	serviceName = "PersonService")
public class PersonResourceImpl implements IPersonResource {
	private final PersonDao personDao = PersonDao.getInstance();

	@Override
	public List<Person> readAllPersons() {
		return personDao.readAll();
	}

	@Override
	public ResourceResponse createPerson(Person p) {
		p.setPersonId(0);
		p = personDao.create(p);
		if (p != null)
			return ResourceResponse.ok(p.getPersonId());
		else
			return ResourceResponse.notModified();
	}

	@Override
	public Person readPerson( int id) {
		return personDao.read(id);
	}

	@Override
	public ResourceResponse updatePerson(int id, Person p) {
		Person oldP = personDao.read(id);
		if (oldP == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		p.setPersonId(id);
		personDao.update(p);
		return ResourceResponse.ok(p.getPersonId());
	}

	@Override
	public ResourceResponse deletePerson(int id) {
		Person p = personDao.read(id);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		} else {
			personDao.delete(p);
		}
		p = personDao.read(id);
		if (p == null) {
			return ResourceResponse.ok();
		} else {
			return ResourceResponse.notModified();
		}
	}
}
