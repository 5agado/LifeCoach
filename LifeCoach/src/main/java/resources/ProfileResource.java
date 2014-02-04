package resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Goal;
import model.Measure;
import model.Person;
import model.dao.GoalDao;
import model.dao.MeasureDao;
import model.dao.PersonDao;

@Path("/person/{id}/profile/")
public class ProfileResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final MeasureDao measureDao = MeasureDao.getInstance();
	private final GoalDao goalDao = GoalDao.getInstance();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("measures")
	public List<Measure> readProfileMeasures(@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}

		List<Measure> profile = measureDao.readPersonProfile(personId,
				profileType);
		return profile;
	}

	@DELETE
	@Path("measures")
	public Response deleteProfileMeasures(@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		measureDao.deletePersonProfile(personId, profileType);
		return Response.ok().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("goals")
	public List<Goal> readProfileGoals(@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}

		List<Goal> goals = goalDao
				.readPersonProfileGoals(personId, profileType);

		return goals;
	}

	@DELETE
	@Path("goals")
	public Response deleteProfileGoals(@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		goalDao.deletePersonProfileGoals(personId, profileType);

		return Response.ok().build();
	}
}
