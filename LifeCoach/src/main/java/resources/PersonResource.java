package resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Person;
import model.dao.PersonDao;

@Path("person/")
public class PersonResource {
	private final PersonDao personDao = PersonDao.getInstance();
	
	@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Person> readAllPersons() {
		return personDao.readAll();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createPerson(Person p) {
		p.setPersonId(0);
		p = personDao.create(p);
		if (p != null)
			return Response.ok(p, MediaType.APPLICATION_XML).build();
		else
			return Response.notModified().build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{id}")
	public Person readPerson(@PathParam("id") int id) {
		return personDao.read(id);
	}
	
	@PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{id}")
	public Response updatePerson(@PathParam("id") int id, Person p) {
		Person oldP = personDao.read(id);
		if (oldP == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		p.setPersonId(id);
		personDao.update(p);
		return Response.ok(p, MediaType.APPLICATION_XML).build();
	}
	
	@DELETE
    @Path("{id}")
	public Response deletePerson(@PathParam("id") int id) {
		Person p = personDao.read(id);
		if (p == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		else{
			personDao.delete(p);
		}
		p = personDao.read(id);
		if (p == null){
			return Response.ok().build();
		}
		else{
			return Response.notModified().build();
		}
	}
}
