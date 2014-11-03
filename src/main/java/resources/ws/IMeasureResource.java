package resources.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Measure;
import model.ResourceResponse;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project")
public interface IMeasureResource {

	@WebMethod(operationName = "readAllMeasures")
	public List<Measure> readAllMeasures(int personId, String measureName,
			String before, String after);

	@WebMethod(operationName = "createMeasure")
	public ResourceResponse createMeasure(Measure measure, int personId,
			String measureName);

	@WebMethod(operationName = "readMeasure")
	public Measure readMeasure(int personId, String measureName, int measureId);

	@WebMethod(operationName = "updateMeasure")
	public ResourceResponse updateMeasure(Measure measure, int personId,
			String measureName, int measureId);

	@WebMethod(operationName = "deleteMeasure")
	public ResourceResponse deleteMeasure(int personId, String measureName,
			int measureId);

}