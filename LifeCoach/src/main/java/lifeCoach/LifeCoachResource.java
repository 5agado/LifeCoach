package lifeCoach;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lifeCoach.model.LifeCoachReport;
import model.Goal;
import model.Measure;
import model.Person;
import client.ProfileClient;
import client.QuotesClient;

@Path("/lifeCoach/")
public class LifeCoachResource {
	private ProfileClient client = new ProfileClient("http://localhost:8080/SDE_Final_Project/rest");
	private QuotesClient quotesClient = new QuotesClient();
	
	@GET
	@Path("report/person/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public LifeCoachReport readPersonReport(@PathParam("id") int personId, @QueryParam("profileType") String profileType) {
		LifeCoachReport report = new LifeCoachReport();
		
		Person person = client.readPerson(personId);
		if (person == null){
			return null;
		}
		
		if (profileType == null){
			profileType = "";
		}
		
		//TODO management of wrong response (null)
		List<Measure> measures = client.readProfile(personId, profileType);
		report.setMeasures(measures);
		
		List<Goal> goals = client.readProfileGoals(personId, profileType);
		report.setGoals(goals);
		
		List<String> goalsStates = new ArrayList<String>(); 
		
		for (Goal g : goals){
			for (Measure m : measures){
				if (g.getMeasureDefinition().getMeasureDefId() == m.getMeasureDefinition().getMeasureDefId()){
					String goalState = LifeCoachLogic.computeAndGetGoalCurrentState(g, m);
					goalsStates.add(goalState);
					break;
				}
			}
		}
		
		report.setGoalsStates(goalsStates);
		
		String quote = quotesClient.getRandomQuote();
		report.setMotivational(quote);
		
		return report;
	}
}
