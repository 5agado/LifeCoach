package resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Goal;
import model.Measure;
import model.MeasureDefinition;
import model.Person;
import model.dao.GoalDao;
import model.dao.MeasureDao;
import model.dao.MeasureDefinitionDao;
import model.dao.PersonDao;

@Path("/person/{id}/profile/")
public class ProfileResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final MeasureDao measureDao = MeasureDao.getInstance();
	private final GoalDao goalDao = GoalDao.getInstance();
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("measures")
	public List<Measure> readProfileMeasures(@PathParam("id") int personId,
			@QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}		
		
		//TODO check if empty is the case
		if (profileType == null || profileType.isEmpty()){
			return measureDao.readAllByPerson(personId);
		}
		
		List<Measure> profile = measureDao.readPersonProfile(personId, profileType);
		return profile;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("goals")
	public List<Goal> readProfileGoals(@PathParam("id") int personId,
			@QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}
		
		//TODO check if empty is the case
		if (profileType == null || profileType.isEmpty()){
			return goalDao.readAllByPerson(personId);
		}
		
		List<MeasureDefinition> list = measureDefinitionDao
				.readByProfileType(profileType);
		if (list.isEmpty()) {
			return null;
		}
		
		List<Goal> goals = new ArrayList<Goal>();
		for (MeasureDefinition def : list){
			List<Goal> subGoals = goalDao.readAllByPersonAndDefinition(personId, def.getMeasureDefId());
			goals.addAll(subGoals);
		}
		
		return goals;
	}
}
