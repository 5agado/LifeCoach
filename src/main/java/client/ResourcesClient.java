package client;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import model.Goal;
import model.Measure;
import model.Person;
import util.Utils;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ResourcesClient extends RESTClient {
	private static final String PROFILE_TYPE_QUERY_NAME = "profileType";
	private static final String REQUEST_URL = "http://localhost:5030";
	//"http://localhost:8080/SDE_Final_Project/rest"; ;

	public ResourcesClient() {
		super(REQUEST_URL);
	}

	public ResourcesClient(String requestUrl) {
		super(requestUrl);
	}

	public void createMeasure(Measure measure, int personId) {
		executePOST("/person/" + personId + "/"
				+ measure.getMeasureDefinition().getMeasureName(), measure);
	}

	public Person readPerson(int personId) {
		ClientResponse response = executeGET("/person/" + personId,
				new MultivaluedMapImpl());
		Person person = extractEntity(response, Person.class);
		return person;
	}

	public List<Measure> readProfileMeasures(int personId, String profileType) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add(PROFILE_TYPE_QUERY_NAME, profileType);
		ClientResponse response = executeGET("/person/" + personId
				+ "/profile/measures", queryParams);
		List<Measure> measures = extractEntityWrapper(response, Measure.class);
		return measures;
	}

	public List<Measure> readMeasuresByDate(int personId, String measure,
			Date before, Date after) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		if (before != null) {
			queryParams.add("before", Utils.convertDateToString(before));
		}
		if (after != null) {
			queryParams.add("after", Utils.convertDateToString(after));
		}
		ClientResponse response = executeGET("/person/" + personId + "/"
				+ measure, queryParams);
		List<Measure> measures = extractEntityWrapper(response, Measure.class);
		return measures;
	}

	public List<Goal> readProfileGoals(int personId, String profileType) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add(PROFILE_TYPE_QUERY_NAME, profileType);
		ClientResponse response = executeGET("/person/" + personId
				+ "/profile/goals", queryParams);
		List<Goal> goals = extractEntityWrapper(response, Goal.class);
		return goals;
	}

	public Goal readGoal(int personId, String measure, int goalId) {
		ClientResponse response = executeGET("/person/" + personId + "/"
				+ measure + "/goal/" + goalId, new MultivaluedMapImpl());
		Goal goal = extractEntity(response, Goal.class);
		return goal;
	}

	public List<Goal> readGoalsByMeasure(int personId, String measure) {
		ClientResponse response = executeGET("/person/" + personId + "/"
				+ measure + "/goal", new MultivaluedMapImpl());
		List<Goal> goals = extractEntityWrapper(response, Goal.class);
		return goals;
	}

	public String getRandomQuote() {
		ClientResponse response = executeGET("/phrases/quote",
				new MultivaluedMapImpl());
		String quote = response.getEntity(String.class);
		return quote;
	}

	public String getMotivational(String personName) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("personName", personName);
		ClientResponse response = executeGET("/phrases/motivational",
				queryParams);
		String phrase = response.getEntity(String.class);
		return phrase;
	}

	public void deleteProfileGoals(int personId, String profileType) {
		executeDELETE("/person/" + personId + "/profile/goals?"
				+ PROFILE_TYPE_QUERY_NAME + "=" + profileType);
	}

	public void deleteGoal(int personId, String measureName, int goalId) {
		executeDELETE("/person/" + personId + "/" + measureName + "/goal/"
				+ goalId);
	}
}
