package lifeCoach;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lifeCoach.model.LifeCoachMeasure;
import lifeCoach.model.LifeCoachReport;
import lifeCoach.model.LifeCoachReportStatistics;
import model.Person;
import client.LifeCoachLogicClient;
import client.ResourcesClient;

@Path("/lifeCoach/person/{id}/")
public class LifeCoachResource {
	private ResourcesClient client = new ResourcesClient();
	private LifeCoachLogicClient logicClient = new LifeCoachLogicClient();

	@GET
	@Path("report")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public LifeCoachReport readPersonReport(
			@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType,
			@DefaultValue("") @QueryParam("goalStatusFilter") String goalStatusFilter) {
		LifeCoachReport report = new LifeCoachReport();

		Person person = client.readPerson(personId);
		if (person == null) {
			return null;
		}

		report.setPerson(person);
		report.setTimestamp(new Date());

		List<LifeCoachMeasure> measures = logicClient.getLifeCoachMeasures(
				personId, profileType, goalStatusFilter);
		report.setMeasures(measures);

		if (goalStatusFilter.isEmpty()) {
			LifeCoachReportStatistics statistics = logicClient
					.getReportOverallStatistics(personId, profileType);

			report.setStatistics(statistics);

			// we provide a motivational phrase only if the person
			// dosn't have all goal in SUCCESS state
			if (!(statistics.getSuccesses() == statistics.getNumGoals())) {
				String motivational = client.getMotivational(person
						.getFirstname());
				report.setMotivational(motivational);
			}
		}

		String quote = client.getRandomQuote();
		report.setQuote(quote);

		return report;
	}

	@DELETE
	@Path("report/goals")
	public Response cleanPersonReport(
			@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType,
			@DefaultValue("") @QueryParam("goalStatusFilter") String goalStatusFilter) {

		Person person = client.readPerson(personId);
		if (person == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		if (goalStatusFilter.isEmpty()) {
			client.deleteProfileGoals(personId, profileType);
			return Response.ok().build();
		}

		List<LifeCoachMeasure> measures = logicClient.getLifeCoachMeasures(
				personId, profileType, goalStatusFilter);

		if (measures.isEmpty()) {
			return Response.notModified().build();
		}

		for (LifeCoachMeasure m : measures) {
			client.deleteGoal(personId, m.getMeasureName(), m.getGoal()
					.getGoalId());
		}

		return Response.ok().build();
	}

	@GET
	@Path("statistics")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public LifeCoachReportStatistics readPersonStatistics(
			@PathParam("id") int personId,
			@DefaultValue("") @QueryParam("profileType") String profileType) {

		Person person = client.readPerson(personId);
		if (person == null) {
			return null;
		}

		LifeCoachReportStatistics statistics = new LifeCoachReportStatistics();

		statistics = logicClient.getReportOverallStatistics(personId,
				profileType);
		return statistics;
	}
}
