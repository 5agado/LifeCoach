package client;

import java.io.StringReader;
import java.util.List;

import model.Goal;
import model.Measure;
import util.Serializer;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ProfileClient extends RESTClient {

	public ProfileClient(String requestUrl) {
		super(requestUrl);
	}

	//We didn't retrive the measureDef complete info (e.g defId), instead we used directly the measure name
	public void createMeasure(Measure measure, int personId) {
		String measureInput = Serializer.marshalString(measure);
		String response = executePOSTWithRequestEntity("person/" + personId + "/"
				+ measure.getMeasureDefinition().getMeasureName(), measureInput);
	}
	
	public List<Measure> readProfile(int personId, String profileType){
		String stringProfile = readProfileAsString(personId, profileType); 
		StringReader reader = new StringReader(stringProfile);
		List<Measure> profile = Serializer.unmarshalWrapper(Measure.class, reader);
		return profile;
	}
	
	public String readProfileAsString(int personId, String profileType){
		String profile = executeGET("/person/" + personId + "/profile/" + profileType + "/measures", new MultivaluedMapImpl()); 
		return profile;
	}
	
	public List<Goal> readProfileGoals(int personId, String profileType){
		String stringGoals = readProfileGoalsAsString(personId, profileType); 
		StringReader reader = new StringReader(stringGoals);
		List<Goal> goals = Serializer.unmarshalWrapper(Goal.class, reader);
		return goals;
	}
	
	public String readProfileGoalsAsString(int personId, String profileType){
		String goals = executeGET("/person/" + personId + "/profile/" + profileType + "goals", new MultivaluedMapImpl()); 
		return goals;
	}
}
