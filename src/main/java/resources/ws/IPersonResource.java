package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Person;
import model.ResourceResponse;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project")
public interface IPersonResource {

	@WebMethod(operationName = "readAllPersons")
	public List<Person> readAllPersons();

	@WebMethod(operationName = "createPerson")
	public ResourceResponse createPerson(Person p);

	@WebMethod(operationName = "readPerson")
	public Person readPerson(int id);

	@WebMethod(operationName = "updatePerson")
	public ResourceResponse updatePerson(int id, Person p);

	@WebMethod(operationName = "deletePerson")
	public ResourceResponse deletePerson(int id);

}