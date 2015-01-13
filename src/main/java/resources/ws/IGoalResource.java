package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import model.Goal;
import model.ResourceResponse;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project")
public interface IGoalResource {
	
	@WebMethod(operationName = "readAllGoals")
	public List<Goal> readAllGoals(int personId, String measureName);

	@WebMethod(operationName = "createGoal")
	public ResourceResponse createGoal(Goal goal, int personId,
			String measureName);
	
	@WebMethod(operationName = "readGoal")
	public Goal readGoal(int personId, String measureName, int goalId);

	@WebMethod(operationName = "updateGoal")
	public ResourceResponse updateGoal(Goal goal, int personId,
			String measureName, int goalId);

	@WebMethod(operationName = "deleteGoal")
	public ResourceResponse deleteGoal(int personId, String measureName,
			int goalId);

}