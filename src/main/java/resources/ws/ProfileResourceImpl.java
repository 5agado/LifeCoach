package resources.ws;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import model.Goal;
import model.Measure;
import model.Person;
import model.ResourceResponse;
import model.ResourceResponse.Status;
import model.dao.GoalDao;
import model.dao.MeasureDao;
import model.dao.PersonDao;

//
@WebService(targetNamespace = "http://www.sagado.edu/soi/project",
	endpointInterface = "resources.ws.IProfileResource")
public class ProfileResourceImpl implements IProfileResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final MeasureDao measureDao = MeasureDao.getInstance();
	private final GoalDao goalDao = GoalDao.getInstance();

	@Override
	public List<Measure> readProfileMeasures(int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}

		List<Measure> profile = measureDao.readPersonProfile(personId,
				profileType);
		return profile;
	}

	@Override
	public ResourceResponse deleteProfileMeasures(int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}

		measureDao.deletePersonProfile(personId, profileType);
		return ResourceResponse.ok();
	}

	@Override
	public List<Goal> readProfileGoals(int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}

		List<Goal> goals = goalDao
				.readPersonProfileGoals(personId, profileType);

		return goals;
	}

	@Override
	public ResourceResponse deleteProfileGoals(int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {
		Person p = personDao.read(personId);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}

		goalDao.deletePersonProfileGoals(personId, profileType);

		return ResourceResponse.ok();
	}
}
