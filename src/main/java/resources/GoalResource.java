package resources;

import java.util.Date;
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

import model.Goal;
import model.MeasureDefinition;
import model.Person;
import model.dao.GoalDao;
import model.dao.MeasureDefinitionDao;
import model.dao.PersonDao;

@Path("/person/{id}/{measure}/goal/")
public class GoalResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final GoalDao goalDao = GoalDao.getInstance();
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Goal> readAllGoals(@PathParam("id") int personId,
			@PathParam("measure") String measureName) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return null;
		}
		return goalDao.readAllByPersonAndDefinition(personId, list.get(0)
				.getMeasureDefId());
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createGoal(Goal goal, @PathParam("id") int personId,
			@PathParam("measure") String measureName) {
		Person p = personDao.read(personId);
		if (p == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!isGoalValid(goal)) {
			return Response.notAcceptable(null).build();
		}
		goal.setTimestamp(new Date());
		goal.setMeasureDefinition(list.get(0));
		goal.setPerson(p);
		goal.setGoalId(0);
		goal = goalDao.create(goal);
		if (goal != null)
			return Response.ok(goal, MediaType.APPLICATION_XML).build();
		else
			return Response.notModified().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{gid}")
	public Goal readGoal(@PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@PathParam("gid") int goalId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return null;
		}
		Goal g = goalDao.readByConstraints(personId, list.get(0)
				.getMeasureDefId(), goalId);
		return g;
	}

	@PUT
	@Path("{gid}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateGoal(Goal goal, @PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@PathParam("gid") int goalId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Goal g = goalDao.readByConstraints(personId, list.get(0)
				.getMeasureDefId(), goalId);
		if (g == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Person p = personDao.read(personId);
		if (p == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!isGoalValid(goal)) {
			return Response.notAcceptable(null).build();
		}

		// Here we decided to keep the previous timestamp
		// i.e.: when the goal we are going to modify has been created
		goal.setTimestamp(g.getTimestamp());
		goal.setMeasureDefinition(list.get(0));
		goal.setGoalId(goalId);
		goal.setPerson(p);
		goalDao.update(goal);
		return Response.ok(goal, MediaType.APPLICATION_XML).build();
	}

	@DELETE
	@Path("{gid}")
	public Response deleteGoal(@PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@PathParam("gid") int goalId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Goal g = goalDao.readByConstraints(personId, list.get(0)
				.getMeasureDefId(), goalId);
		if (g == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		goalDao.delete(g);
		g = goalDao.read(goalId);
		if (g == null) {
			return Response.ok().build();
		} else
			return Response.notModified().build();
	}

	private boolean isGoalValid(Goal g) {
		String val = g.getValue();
		if (val == null || val.isEmpty()) {
			return false;
		}
		try {
			Double.valueOf(val);
		} catch (NumberFormatException e) {
			return false;
		}

		if (g.getExpDate() == null) {
			return false;
		}

		return true;
	}
}
