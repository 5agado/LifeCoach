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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.MeasureDefinition;
import model.ResourceResponse;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project")
public interface IMeasureDefinitionResource {

	@WebMethod(operationName = "readMeasureDefinitionByProfileType")
	public List<MeasureDefinition> readMeasureDefinitionByProfileType(
			String profileType);

	@WebMethod(operationName = "createMeasureDefinition")
	public ResourceResponse createMeasureDefinition(MeasureDefinition mDef);

	@WebMethod(operationName = "readMeasureDefinition")
	public MeasureDefinition readMeasureDefinition(int id);

	@WebMethod(operationName = "updateMeasureDefinition")
	public ResourceResponse updateMeasureDefinition(int id, MeasureDefinition mDef);

	@WebMethod(operationName = "deleteMeasureDefinition")
	public ResourceResponse deleteMeasureDefinition(int id);

}