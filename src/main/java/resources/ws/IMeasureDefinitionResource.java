package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

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