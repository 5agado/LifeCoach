package lifeCoach;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Goal;
import client.ProfileClient;

@Path("/lifeCoach/")
public class LifeCoachResource {
	private ProfileClient client = new ProfileClient("http://localhost:8080/SDE_Final_Project/rest");
	
	@GET
	@Path("report/person/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public LifeCoachReport readPersonReport(@PathParam("id") int personId, @QueryParam("profileType") String profileType) {
		LifeCoachReport report = new LifeCoachReport();
		
		if (profileType.isEmpty()){
			//TODO
			return report;
		}
		
		List<Goal> goals = client.readProfileGoals(personId, profileType);
		report.setGoals(goals);
		
		return report;
	}	
}
