package lifeCoach;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.Goal;
import model.Measure;

import org.joda.time.Instant;

public class LifeCoachLogic {
	private final static Logger LOGGER = Logger.getLogger(LifeCoachLogic.class.getName());
	private static final String LESS_COMPARATOR = "minor";
	private static final String GREATER_COMPARATOR = "greater";
	private LifeCoachLogic() {
		throw new AssertionError();
	}
	
	public static String computeAndGetGoalCurrentState(final Goal goal, final Measure measure){
		Double currentValue = Double.valueOf(measure.getValue());
		Double espectedValue = Double.valueOf(goal.getValue());
		Double valueDiff = 0d;
		switch (goal.getComparator()) {
		case LESS_COMPARATOR:
			valueDiff = currentValue - espectedValue;
			break;
		case GREATER_COMPARATOR:
			valueDiff = espectedValue - currentValue;
			break;
		default:
			LOGGER.log(Level.WARNING, "Unexpected goal comparator");
			break;
		}
		
		String goalState;
		boolean expired = true;
		long expDateTime = goal.getExpDate().getTime();
		long currentDateTime = new Instant().getMillis();
		if (expDateTime > currentDateTime){
			expired = false;
			long interval = expDateTime - currentDateTime;
			long entireInterval = expDateTime - goal.getTimestamp().getTime();
			//Interval interval = new Interval(currentDateTime, expDateTime);
			//Interval entireInterval = new Interval(goal.getTimestamp().getTime(), expDateTime);
			if (valueDiff <= 0){
				goalState = "SUCCESS by " + Math.abs(valueDiff);
			}
			else {
				long intervalPercentage = (entireInterval/interval)*100;
				goalState = "Passed time " + intervalPercentage;
			}
		}
		else {
			if (valueDiff <= 0){
				goalState = "SUCCESS by " + Math.abs(valueDiff);
			}
			else {
				goalState = "FAILED by " + valueDiff;
			}
		}
		
		return goalState;
		
	}
}
