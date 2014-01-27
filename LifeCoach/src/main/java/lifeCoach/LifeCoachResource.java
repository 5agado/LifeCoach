package lifeCoach;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lifeCoach.model.LifeCoachMeasure;
import lifeCoach.model.LifeCoachReport;
import lifeCoach.model.LifeCoachReportStatistics;
import model.Goal;
import model.Measure;
import model.Person;
import client.ProfileClient;
import client.QuotesClient;

@Path("/lifeCoach/")
public class LifeCoachResource {
	private ProfileClient client = new ProfileClient(
			"http://localhost:8080/SDE_Final_Project/rest");
	private QuotesClient quotesClient = new QuotesClient();

	@GET
	@Path("report/person/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public LifeCoachReport readPersonReport(@PathParam("id") int personId,
			@QueryParam("profileType") String profileType) {
		LifeCoachReport report = new LifeCoachReport();

		Person person = client.readPerson(personId);
		if (person == null) {
			return null;
		}

		report.setPerson(person);
		report.setTimestamp(new Date());

		// If no profileType is provided we compute all the person goals
		if (profileType == null) {
			profileType = "";
		}

		List<LifeCoachMeasure> measures = getLifeCoachProfileMeasures(personId,
				profileType);
		report.setMeasures(measures);

		LifeCoachReportStatistics statistics = LifeCoachLogic
				.computeAndGetReportOverallStatistics(report);
		report.setStatistics(statistics);

		String quote = quotesClient.getRandomQuote();
		report.setMotivational(quote);

		return report;
	}

	@GET
	@Path("statistics/person/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public LifeCoachReportStatistics readPersonStatistics(
			@PathParam("id") int personId,
			@QueryParam("profileType") String profileType) {

		Person person = client.readPerson(personId);
		if (person == null) {
			return null;
		}

		LifeCoachReportStatistics statistics = new LifeCoachReportStatistics();

		if (profileType == null) {
			profileType = "";
		}

		List<LifeCoachMeasure> measures = getLifeCoachProfileMeasures(personId,
				profileType);

		LifeCoachReport report = new LifeCoachReport();
		report.setMeasures(measures);

		statistics = LifeCoachLogic
				.computeAndGetReportOverallStatistics(report);
		return statistics;
	}

	private List<LifeCoachMeasure> getLifeCoachProfileMeasures(int personId,
			String profileType) {
		List<Measure> remoteMeasures = client
				.readProfile(personId, profileType);
		List<LifeCoachMeasure> measures = new ArrayList<LifeCoachMeasure>();
		if (remoteMeasures == null) {
			return measures;
		}

		for (Measure m : remoteMeasures) {
			LifeCoachMeasure lifeM = new LifeCoachMeasure();
			List<String> goalsStatusDescription = new ArrayList<String>();
			lifeM.setMeasureName(m.getMeasureDefinition().getMeasureName());
			lifeM.setValue(m.getValue());
			List<Goal> goals = client.readGoalsByMeasure(personId, m
					.getMeasureDefinition().getMeasureName());
			for (Goal g : goals) {
				String goalState = LifeCoachLogic
						.computeAndGetGoalCurrentState(g, m);
				goalsStatusDescription.add(goalState);
			}
			lifeM.setGoals(goals);
			lifeM.setGoalsStatusDescription(goalsStatusDescription);
			measures.add(lifeM);
		}

		return measures;
	}
}
