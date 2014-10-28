package businessLogic;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import model.Goal;
import model.Measure;
import model.dao.GoalDao;
import model.dao.MeasureDao;

import org.junit.Test;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.XMLParser;

public class TestLifeCoachLogic {
	@Test
	public void TestGoalStateComputation() {
		Goal goal = GoalDao.getInstance().read(2);
		assertNotNull("Goal is not here", goal);
		Measure measure = MeasureDao.getInstance().read(23);
		assertNotNull("Measure is not here", measure);
		// System.out.println(LifeCoachLogic.computeAndGetGoalCurrentState(goal,
		// measure));
	}

	@Test
	public void ComputeIntervalPercentage() throws NoSuchMethodException,
			SecurityException {
		// Method method =
		// LifeCoachLogic.class.getDeclaredMethod("computeIntervalPercentage",
		// Long.class, Long.class);
		// method.setAccessible(true);
		//		Calendar c = Calendar.getInstance();
		//		c.set(2014, 1, 11);
		//		Date d1 = c.getTime();
		//		c.set(2014, 2, 1);
		//		Date d2 = c.getTime();
		//		c.set(2014, 2, 2);
		//		Date d3 = c.getTime();

		// System.out.println(LifeCoachLogic.computeIntervalPercentage(d1, d2,
		// d3));
	}

	@Test
	public void testMotivational() {
		Random r = new Random();
		XMLParser parser = null;
		try {
			parser = new XMLParser("src/main/resources/motivationalPhrases.xml");
		} catch (IOException | SAXException | ParserConfigurationException e) {
			System.out.println("No motivational");
		}
		NodeList nodeList = parser.readNodeList("/phrases/phrase");
		nodeList.item(r.nextInt(nodeList.getLength()));
	}
}
