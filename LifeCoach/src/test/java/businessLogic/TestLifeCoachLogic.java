package businessLogic;

import static org.junit.Assert.assertNotNull;
import lifeCoach.LifeCoachLogic;
import model.Goal;
import model.Measure;
import model.dao.GoalDao;
import model.dao.MeasureDao;

import org.junit.Test;

public class TestLifeCoachLogic {
	@Test
	public void TestGoalStateComputation() {
		Goal goal = GoalDao.getInstance().read(2);
		assertNotNull("Goal is not here", goal);
		Measure measure = MeasureDao.getInstance().read(23);
		assertNotNull("Measure is not here", measure);
		System.out.println(LifeCoachLogic.computeAndGetGoalCurrentState(goal, measure));
	}
}
