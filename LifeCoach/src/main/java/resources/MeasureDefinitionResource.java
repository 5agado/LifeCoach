package resources;

import java.util.List;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.MeasureDefinition;
import model.dao.MeasureDefinitionDao;

@Path("/measureDefinition/")
public class MeasureDefinitionResource {
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<MeasureDefinition> readMeasureDefinitionByProfileType(
			@QueryParam("profileType") String profileType) {
		if (profileType == null) {
			return measureDefinitionDao.readAll();
		} else
			return measureDefinitionDao.readByProfileType(profileType);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createMeasureDefinition(MeasureDefinition mDef) {
		if (!isMeasureDefValid(mDef)) {
			return Response.notAcceptable(null).build();
		}
		List<MeasureDefinition> list = measureDefinitionDao.readByName(mDef
				.getMeasureName());
		if (!list.isEmpty()) {
			return Response.status(Status.CONFLICT).build();
		}
		mDef.setMeasureDefId(0);
		mDef = measureDefinitionDao.create(mDef);
		if (mDef != null)
			return Response.ok(mDef, MediaType.APPLICATION_XML).build();
		else
			return Response.notModified().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{id}")
	public MeasureDefinition readMeasureDefinition(@PathParam("id") int id) {
		return measureDefinitionDao.read(id);
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{id}")
	public Response updateMeasureDefinition(@PathParam("id") int id,
			MeasureDefinition mDef) {
		MeasureDefinition oldDef = measureDefinitionDao.read(id);
		if (oldDef == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!isMeasureDefValid(mDef)) {
			return Response.notAcceptable(null).build();
		}
		List<MeasureDefinition> list = measureDefinitionDao.readByName(mDef
				.getMeasureName());
		if (!list.isEmpty()) {
			return Response.status(Status.CONFLICT).build();
		}
		mDef.setMeasureDefId(id);
		measureDefinitionDao.update(mDef);
		return Response.ok(mDef, MediaType.APPLICATION_XML).build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteMeasureDefinition(@PathParam("id") int id) {
		MeasureDefinition mDef = measureDefinitionDao.read(id);
		if (mDef == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			measureDefinitionDao.delete(mDef);
		}
		mDef = measureDefinitionDao.read(id);
		if (mDef == null) {
			return Response.ok().build();
		} else {
			return Response.notModified().build();
		}

	}

	private boolean isMeasureDefValid(MeasureDefinition def) {
		if (def.getMeasureName() == null || def.getMeasureName().isEmpty()) {
			return false;
		}
		if (def.getProfileType() == null || def.getProfileType().isEmpty()) {
			return false;
		}

		return true;
	}
}
