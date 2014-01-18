package resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import client.Stands4Client;
import model.Measure;
import model.Person;
import model.dao.MeasureDao;
import model.dao.PersonDao;

@Path("/person/{id}/profile/")
public class ProfileResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final MeasureDao measureDao = MeasureDao.getInstance();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{profileType}")
	public List<Measure> readProfile(@PathParam("id") int personId,
			@PathParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			//TODO check ??better to return always a Reponse (with incorporated object)
			return null;
		}		
		List<Measure> profile = measureDao.readPersonProfile(personId, profileType);
		return profile;
	}
	
	//TODO tmp
	//move this in a more conson place, add an object for motivational phrase reppresentation
	//and convert from different sources
	@GET
	@Path("motivational")
	public String getMotivationalPhrase() {
		Stands4Client client = new Stands4Client();
		String res = client.getRandomQuote();
		return res;
	}
}
