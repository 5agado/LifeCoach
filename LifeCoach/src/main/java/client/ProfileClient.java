package client;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import model.Goal;
import model.Measure;
import model.Person;
import util.Serializer;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ProfileClient extends RESTClient {
	private static final String PROFILE_TYPE_QUERY_NAME = "profileType";

	public ProfileClient(String requestUrl) {
		super(requestUrl);
	}

	//We didn't retrive the measureDef complete info (e.g defId), instead we used directly the measure name
	public void createMeasure(Measure measure, int personId) {
		String measureInput = Serializer.marshalAsString(measure);
		executePOSTWithRequestEntity("person/" + personId + "/" + measure.getMeasureDefinition().getMeasureName(), measureInput);
		//String response = extractEntityAsString(executePOSTWithRequestEntity("person/" + personId + "/"
		//+ measure.getMeasureDefinition().getMeasureName(), measureInput));
	}
	
	public Person readPerson(int personId){
		ClientResponse response = executeGET("/person/" + personId, new MultivaluedMapImpl());
		Person person = extractEntity(response, Person.class);
		return person;
	}
	
	public List<Measure> readProfile(int personId, String profileType){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl(); 
		queryParams.add(PROFILE_TYPE_QUERY_NAME, profileType);
		ClientResponse response = executeGET("/person/" + personId + "/profile/measures", queryParams);
		List<Measure> profile = extractEntityWrapper(response, Measure.class);
		return profile;
	}
	
	public List<Goal> readProfileGoals(int personId, String profileType){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl(); 
		queryParams.add(PROFILE_TYPE_QUERY_NAME, profileType);
		ClientResponse response = executeGET("/person/" + personId + "/profile/goals", queryParams);
		List<Goal> goals = extractEntityWrapper(response, Goal.class);
		return goals;
	}
	
	public List<Goal> readGoalsByMeasure(int personId, String measure){
		ClientResponse response = executeGET("/person/" + personId + "/" + measure + "/goal", new MultivaluedMapImpl());
		List<Goal> goals = extractEntityWrapper(response, Goal.class);
		return goals;
	}
}
