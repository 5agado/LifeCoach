package lifeCoach;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lifeCoach.model.LifeCoachMeasure;
import lifeCoach.model.LifeCoachReport;
import lifeCoach.model.LifeCoachReportStatistics;
import model.Goal;
import model.Measure;

public class LifeCoachLogic {
	private final static Logger LOGGER = Logger.getLogger(LifeCoachLogic.class
			.getName());
	private static final String LESS_COMPARATOR = "LT";
	private static final String GREATER_COMPARATOR = "GT";

	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_PROGRESS = "IN PROGRESS";
	private static final String STATUS_FAILURE = "FAILURE";

	private LifeCoachLogic() {
		throw new AssertionError();
	}

	public static String computeAndGetGoalCurrentState(final Goal goal,
			final Measure measure) {
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

	public static LifeCoachReportStatistics computeAndGetReportOverallStatistics(
			LifeCoachReport report) {
		List<LifeCoachMeasure> measures = report.getMeasures();
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

	// TODO private
	public static double computeIntervalPercentage(Date before, Date middle,
			Date after) {
		long beforeMillis = before.getTime();
		long middleMillis = middle.getTime();
		long afterMillis = after.getTime();
		if (middleMillis < afterMillis && beforeMillis < middleMillis) {
			long subInterval = middleMillis - beforeMillis;
			System.out.println(subInterval);
			long interval = afterMillis - beforeMillis;
			System.out.println(interval);
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
