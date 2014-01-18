package client;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;

import util.Serializer;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import model.Goal;
import model.ListWrapper;
import model.Measure;
import model.MeasureDefinition;

public class ProfileClient extends RESTClient {

	public ProfileClient(String requestUrl) {
		super(requestUrl);
	}

	//TODO Measure as input
	//We didn't retrive the measureDef complete info (e.g defId), instead we used directly the measure name
	public void createMeasure(Measure measure, int personId) {
		String measureInput = Serializer.marshalString(Measure.class, measure);
		String response = executePOSTWithRequestEntity("person/" + personId + "/"
				+ measure.getMeasureDefinition().getMeasureName(), measureInput);
		//TODO use log
		System.out.println(response);
	}
	
	public String readProfile(int personId, String profileType){
		String profile = executeGET("/person/" + personId + "/profile/" + profileType, new MultivaluedMapImpl()); 
		System.out.println(profile);
//		StringReader reader = new StringReader(list);
//		Profile<Measure> profile = (Profile<Measure>) unmarshall(Profile.class, reader);
		return profile;
	}
	
	public List<Goal> readProfileGoals(int personId, String profileType){
		//GET all measureDef for the profile
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl(); 
		queryParams.add("profileType", profileType);
		String response = executeGET("/measureDefinition/", queryParams);
		StringReader reader = new StringReader(response);
		List<MeasureDefinition> defs = Serializer.unmarshalWrapper(MeasureDefinition.class, reader);
		
		//GET all goals (i.e. all goals for each measure)
		List<Goal> profileGoals = new ArrayList<Goal>();
		setDebugEnabled(true);
		for (MeasureDefinition def : defs){
			//TODO check response validity
			response = executeGET("/person/" + personId + "/" + def.getMeasureName() + "/goal/", new MultivaluedMapImpl());
			System.out.println(response);
			reader = new StringReader(response);
			List<Goal> goals = Serializer.unmarshalWrapper(Goal.class, reader);
			profileGoals.addAll(goals);
		}
		
		return profileGoals;
	}
}
