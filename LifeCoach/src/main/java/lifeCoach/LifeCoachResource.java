package lifeCoach;

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
import model.Person;
import client.LifeCoachLogicClient;
import client.ResourcesClient;

@Path("/lifeCoach/")
public class LifeCoachResource {
	private ResourcesClient client = new ResourcesClient();
	private LifeCoachLogicClient logicClient = new LifeCoachLogicClient();
	//TODO use default value
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

		List<LifeCoachMeasure> measures = logicClient.getLifeCoachMeasures(
				personId, profileType);
		report.setMeasures(measures);

		LifeCoachReportStatistics statistics = logicClient
				.getReportOverallStatistics(personId, profileType);
		report.setStatistics(statistics);

		String quote = client.getRandomQuote();
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

		statistics = logicClient.getReportOverallStatistics(personId,
				profileType);
		return statistics;
	}
}
