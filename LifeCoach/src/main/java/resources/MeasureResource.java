package resources;

import java.util.Date;
import java.util.List;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Measure;
import model.MeasureDefinition;
import model.Person;
import model.dao.MeasureDao;
import model.dao.MeasureDefinitionDao;
import model.dao.PersonDao;
import util.Utils;

@Path("/person/{id}/{measure}/")
public class MeasureResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final MeasureDao measureDao = MeasureDao.getInstance();
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Measure> readAllMeasures(@PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@QueryParam("before") @DefaultValue("") String before,
			@QueryParam("after") @DefaultValue("") String after) {
		Person p = personDao.read(personId);
		if (p == null) {
			return null;
		}
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return null;
		}
		if (!before.isEmpty() || !after.isEmpty()) {
			Date beforeDate = Utils.convertDateFrom(before);
			Date afterDate = Utils.convertDateFrom(after);
			return measureDao.readAllByPersonDefinitionAndDate(personId, list
					.get(0).getMeasureDefId(), beforeDate, afterDate);
		}
		return measureDao.readAllByPersonAndDefinition(personId, list.get(0)
				.getMeasureDefId());
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createMeasure(Measure measure,
			@PathParam("id") int personId,
			@PathParam("measure") String measureName) {
		Person p = personDao.read(personId);
		if (p == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!isMeasureValueValid(measure)) {
			return Response.notAcceptable(null).build();
		}
		measure.setMeasureDefinition(list.get(0));
		measure.setPerson(p);
		measure.setMeasureId(0);
		if (measure.getTimestamp() == null) {
			measure.setTimestamp(new Date());
		}
		measure = measureDao.create(measure);
		if (measure != null)
			return Response.ok(measure, MediaType.APPLICATION_XML).build();
		else
			return Response.notModified().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{mid}")
	public Measure readMeasure(@PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@PathParam("mid") int measureId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return null;
		}
		Measure m = measureDao.readByCostraints(personId, list.get(0)
				.getMeasureDefId(), measureId);
		return m;
	}

	@PUT
	@Path("{mid}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateMeasure(Measure measure,
			@PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@PathParam("mid") int measureId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Measure m = measureDao.readByCostraints(personId, list.get(0)
				.getMeasureDefId(), measureId);
		if (m == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Person p = personDao.read(personId);
		if (p == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!isMeasureValueValid(measure)) {
			return Response.notAcceptable(null).build();
		}
		measure.setMeasureId(measureId);
		measure.setPerson(p);
		measure.setMeasureDefinition(list.get(0));
		if (measure.getTimestamp() == null) {
			measure.setTimestamp(new Date());
		}
		measureDao.update(measure);
		return Response.ok(measure, MediaType.APPLICATION_XML).build();
	}

	@DELETE
	@Path("{mid}")
	public Response deleteMeasure(@PathParam("id") int personId,
			@PathParam("measure") String measureName,
			@PathParam("mid") int measureId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Measure m = measureDao.readByCostraints(personId, list.get(0)
				.getMeasureDefId(), measureId);
		if (m == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		measureDao.delete(m);
		m = measureDao.read(measureId);
		if (m == null) {
			return Response.ok().build();
		} else
			return Response.notModified().build();
	}

	private boolean isMeasureValueValid(Measure m) {
		String val = m.getValue();
		if (val == null || val.isEmpty()) {
			return false;
		}
		try {
			Double.valueOf(val);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
