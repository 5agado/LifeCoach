package resources.ws;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import model.Goal;
import model.MeasureDefinition;
import model.Person;
import model.ResourceResponse;
import model.ResourceResponse.Status;
import model.dao.GoalDao;
import model.dao.MeasureDefinitionDao;
import model.dao.PersonDao;

//@Path("/person/{id}/{measure}/goal/")
@WebService(targetNamespace = "http://www.sagado.edu/soi/project",
	endpointInterface = "resources.ws.IGoalResource")
public class GoalResourceImpl implements IGoalResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final GoalDao goalDao = GoalDao.getInstance();
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();

	@Override
	public List<Goal> readAllGoals(int personId, String measureName) {
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

	@Override
	public ResourceResponse createGoal(Goal goal, int personId, String measureName) {
		Person p = personDao.read(personId);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		if (!isGoalValid(goal)) {
			return ResourceResponse.notAcceptable();
		}
		goal.setTimestamp(new Date());
		goal.setMeasureDefinition(list.get(0));
		goal.setPerson(p);
		goal.setGoalId(0);
		goal = goalDao.create(goal);
		if (goal != null)
			return ResourceResponse.ok(goal.getGoalId());
		else
			return ResourceResponse.notModified();
	}

	@Override
	public Goal readGoal(int personId, String measureName, int goalId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return null;
		}
		Goal g = goalDao.readByConstraints(personId, list.get(0)
				.getMeasureDefId(), goalId);
		return g;
	}

	@Override
	public ResourceResponse updateGoal(Goal goal, int personId, String measureName,
			int goalId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		Goal g = goalDao.readByConstraints(personId, list.get(0)
				.getMeasureDefId(), goalId);
		if (g == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		Person p = personDao.read(personId);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		if (!isGoalValid(goal)) {
			return ResourceResponse.notAcceptable();
		}

		// Here we decided to keep the previous timestamp
		// i.e.: when the goal we are going to modify has been created
		goal.setTimestamp(g.getTimestamp());
		goal.setMeasureDefinition(list.get(0));
		goal.setGoalId(goalId);
		goal.setPerson(p);
		goalDao.update(goal);
		return ResourceResponse.ok(goal.getGoalId());
	}

	@Override
	public ResourceResponse deleteGoal(int personId,
			String measureName,
			int goalId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		Goal g = goalDao.readByConstraints(personId, list.get(0)
				.getMeasureDefId(), goalId);
		if (g == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		goalDao.delete(g);
		g = goalDao.read(goalId);
		if (g == null) {
			return ResourceResponse.ok();
		} else
			return ResourceResponse.notModified();
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
