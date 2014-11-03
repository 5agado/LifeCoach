package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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