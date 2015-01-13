package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import model.Goal;
import model.Measure;
import model.ResourceResponse;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project")
public interface IProfileResource {

	@WebMethod(operationName = "readProfileMeasures")
	public List<Measure> readProfileMeasures(int personId, String profileType);

	@WebMethod(operationName = "deleteProfileMeasures")
	public ResourceResponse deleteProfileMeasures(int personId, String profileType);

	@WebMethod(operationName = "readProfileGoals")
	public List<Goal> readProfileGoals(int personId, String profileType);

	@WebMethod(operationName = "deleteProfileGoals")
	public ResourceResponse deleteProfileGoals(int personId, String profileType);

}