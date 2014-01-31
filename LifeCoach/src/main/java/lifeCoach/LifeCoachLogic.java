package lifeCoach;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lifeCoach.model.LifeCoachMeasure;
import lifeCoach.model.LifeCoachReportStatistics;
import model.Goal;
import model.Measure;
import client.ResourcesClient;

@Path("/lifeCoachLogic/")
public class LifeCoachLogic {
	private ResourcesClient client = new ResourcesClient();
	private final static Logger LOGGER = Logger.getLogger(LifeCoachLogic.class
			.getName());
	private static final String LESS_COMPARATOR = "LT";
	private static final String GREATER_COMPARATOR = "GT";

	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_PROGRESS = "IN PROGRESS";
	private static final String STATUS_FAILURE = "FAILURE";

	@GET
	@Path("measures")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<LifeCoachMeasure> computeAndGetLifeCoachMeasures(
			@QueryParam("personId") int personId,
			@QueryParam("profileType") String profileType,
			@QueryParam("goalStatusFilter") String goalStatusFilter) {
		if (personId == 0) {
			return null;
		}

		if (profileType == null) {
			profileType = "";
		}

		List<LifeCoachMeasure> measures = new ArrayList<LifeCoachMeasure>();
		List<Measure> remoteMeasures = client.readProfileMeasures(personId,
				profileType);
		if (remoteMeasures == null) {
			return measures;
		}

		for (Measure m : remoteMeasures) {
			List<Goal> goals = client.readGoalsByMeasure(personId, m
					.getMeasureDefinition().getMeasureName());
			for (Goal g : goals) {
				List<String> goalsStatusDescription = new ArrayList<String>();
				List<Measure> measuresBeforeGoal = client.readMeasuresByDate(
						personId, m.getMeasureDefinition().getMeasureName(),
						g.getExpDate(), null);
				if (measuresBeforeGoal == null || measuresBeforeGoal.isEmpty()){
					LOGGER.log(Level.FINE, "No recent measured for goal " + g.getGoalId());
					continue;
				}
				Measure goalMostRecentMeasure = measuresBeforeGoal.get(0);
				String goalState = computeAndGetGoalCurrentState(g, goalMostRecentMeasure);
				
				//if the case, filter by goal state
				if (goalStatusFilter!= null && !goalStatusFilter.isEmpty()){
					String[] stateParts = goalState.split("-");
					String status = stateParts[0].trim();
					if (!status.equalsIgnoreCase(goalStatusFilter)){
						continue;
					}
				}				
				
				LifeCoachMeasure lifeM = new LifeCoachMeasure();
				lifeM.setMeasureName(goalMostRecentMeasure.getMeasureDefinition().getMeasureName());
				lifeM.setValue(goalMostRecentMeasure.getValue());
				goalsStatusDescription.add(goalState);
				lifeM.setGoals(goals);
				lifeM.setGoalsStatusDescription(goalsStatusDescription);
				measures.add(lifeM);
			}
		}

		return measures;
	}

	@GET
	@Path("statistics")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public LifeCoachReportStatistics computeAndGetReportOverallStatistics(
			@QueryParam("personId") int personId,
			@QueryParam("profileType") String profileType) {
		List<LifeCoachMeasure> measures = computeAndGetLifeCoachMeasures(
				personId, profileType, null);
		LifeCoachReportStatistics stats = new LifeCoachReportStatistics();
		int numGoals = 0;
		int numS = 0, numF = 0, numP = 0;

		for (LifeCoachMeasure m : measures) {
			List<String> goalsState = m.getGoalsStatusDescription();
			for (String state : goalsState) {
				numGoals++;
				String[] stateParts = state.split("-");
				String status = stateParts[0].trim();
				switch (status) {
				case STATUS_FAILURE:
					numF++;
					break;
				case STATUS_PROGRESS:
					numP++;
					break;
				case STATUS_SUCCESS:
					numS++;
					break;
				default:
					LOGGER.log(Level.INFO,
							"Undefined Status level for report statistics: "
									+ status);
					break;
				}
			}
		}

		stats.setNumGoals(numGoals);
		stats.setSuccesses(numS);
		stats.setFailures(numF);
		stats.setStillInprogress(numP);
		return stats;
	}

	@GET
	@Path("goalState")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getGoalCurrentState(int personId, int goalId,
			String measureName) {
		Goal goal = client.readGoal(personId, measureName, goalId);
		if (goal == null) {
			return "";
		}
		List<Measure> measures = client.readMeasuresByDate(personId,
				measureName, goal.getExpDate(), null);
		if (measures == null || measures.isEmpty()) {
			return "";
		}

		Measure m = measures.get(0);
		return computeAndGetGoalCurrentState(goal, m);
	}

	private static String computeAndGetGoalCurrentState(Goal goal,
			Measure measure) {
		double valueDiff = computeValueDifference(goal, measure);

		String goalState;
		Date currentDate = new Date();
		long expDateTime = goal.getExpDate().getTime();
		long currentDateTime = currentDate.getTime();
		String formattedDiff = String.format("%.2f", Math.abs(valueDiff));
		if (expDateTime > currentDateTime) {
			if (valueDiff <= 0) {
				goalState = STATUS_SUCCESS + " - by " + formattedDiff;
			} else {
				double intervalPercentage = computeIntervalPercentage(
						goal.getTimestamp(), currentDate, goal.getExpDate());
				String formattedPercentage = String.format("%.2f",
						intervalPercentage);
				goalState = STATUS_PROGRESS + " - " + formattedDiff
						+ " still required. Time interval current at "
						+ formattedPercentage + "%";
			}
		} else {
			if (valueDiff <= 0) {
				goalState = STATUS_SUCCESS + " - by " + formattedDiff;
			} else {
				goalState = STATUS_FAILURE + " - by " + formattedDiff;
			}
		}

		return goalState;
	}

	private static double computeValueDifference(final Goal goal,
			final Measure measure) {
		double currentValue = Double.valueOf(measure.getValue());
		double expectedValue = Double.valueOf(goal.getValue());
		double valueDiff = 0;
		switch (goal.getComparator()) {
		case LESS_COMPARATOR:
			valueDiff = currentValue - expectedValue;
			break;
		case GREATER_COMPARATOR:
			valueDiff = expectedValue - currentValue;
			break;
		default:
			LOGGER.log(Level.WARNING, "Unexpected goal comparator");
			break;
		}

		return valueDiff;
	}

	private static double computeIntervalPercentage(Date before, Date middle,
			Date after) {
		long beforeMillis = before.getTime();
		long middleMillis = middle.getTime();
		long afterMillis = after.getTime();
		if (middleMillis < afterMillis && beforeMillis < middleMillis) {
			long subInterval = middleMillis - beforeMillis;
			long interval = afterMillis - beforeMillis;
			double factor = ((subInterval * 1.0) / (interval * 1.0));
			double res = factor * 100.0;
			return res;
		} else {
			LOGGER.log(Level.INFO,
					"Wrong date order for computing interval percentage");
			return 0;
		}
	}
}
