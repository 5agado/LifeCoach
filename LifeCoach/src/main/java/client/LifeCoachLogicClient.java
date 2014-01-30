package client;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import lifeCoach.model.LifeCoachMeasure;
import lifeCoach.model.LifeCoachReportStatistics;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class LifeCoachLogicClient extends RESTClient {
	private static final String REQUEST_URL = "http://localhost:8080/SDE_Final_Project/rest/";
	//"http://localhost:5031/";
	private static final String PROFILE_TYPE_QUERY_NAME = "profileType";
	private static final String PERSONID_TYPE_QUERY_NAME = "personId";

	public LifeCoachLogicClient() {
		super(REQUEST_URL);
	}

	public LifeCoachLogicClient(String requestUrl) {
		super(requestUrl);
	}

	public List<LifeCoachMeasure> getLifeCoachMeasures(int personId,
			String profileType) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add(PROFILE_TYPE_QUERY_NAME, profileType);
		queryParams.add(PERSONID_TYPE_QUERY_NAME, String.valueOf(personId));
		ClientResponse response = executeGET("/lifeCoachLogic/measures",
				queryParams);
		List<LifeCoachMeasure> measures = extractEntityWrapper(response,
				LifeCoachMeasure.class);
		return measures;
	}

	public LifeCoachReportStatistics getReportOverallStatistics(int personId,
			String profileType) {
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add(PROFILE_TYPE_QUERY_NAME, profileType);
		queryParams.add(PERSONID_TYPE_QUERY_NAME, String.valueOf(personId));
		ClientResponse response = executeGET("/lifeCoachLogic/statistics",
				queryParams);
		LifeCoachReportStatistics stats = extractEntity(response,
				LifeCoachReportStatistics.class);
		return stats;
	}
}
