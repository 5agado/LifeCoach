package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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