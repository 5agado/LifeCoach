package client;

import java.util.List;

import model.Goal;

import org.junit.Test;

import util.Serializer;

public class TestResourcesClient {
	private ResourcesClient client = new ResourcesClient();

	@Test
	public void testreadProfileGoals() {
		List<Goal> goals = client.readProfileGoals(2, "blood");
		for (Goal g : goals) {
			String goal = Serializer.marshalAsString(g);
			System.out.println(goal);
		}
	}
}
