package client;

import java.util.List;

import model.Goal;

import org.junit.Test;

import util.Serializer;

public class TestProfileClient {
	private ProfileClient client = new ProfileClient("http://localhost:8080/SDE_Final_Project/rest");
	
	@Test
	public void testreadProfileGoals() {
		List<Goal> goals = client.readProfileGoals(2, "blood");
		for (Goal g : goals){
			String goal = Serializer.marshalString(Goal.class, g);
			System.out.println(goal);
		}
	}
}
