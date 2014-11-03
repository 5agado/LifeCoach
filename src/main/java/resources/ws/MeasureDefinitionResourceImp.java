package resources.ws;

import java.util.List;

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
import model.ResourceResponse.Status;
import model.dao.MeasureDefinitionDao;

@WebService(targetNamespace = "http://www.sagado.edu/soi/project",
	endpointInterface = "resources.ws.IMeasureDefinitionResource")
public class MeasureDefinitionResourceImp implements IMeasureDefinitionResource {
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();

	@Override
	public List<MeasureDefinition> readMeasureDefinitionByProfileType(
			String profileType) {
		if (profileType == null) {
			return measureDefinitionDao.readAll();
		} else
			return measureDefinitionDao.readByProfileType(profileType);
	}

	@Override
	public ResourceResponse createMeasureDefinition(MeasureDefinition mDef) {
		if (!isMeasureDefValid(mDef)) {
			return ResourceResponse.notAcceptable();
		}
		List<MeasureDefinition> list = measureDefinitionDao.readByName(mDef
				.getMeasureName());
		if (!list.isEmpty()) {
			return ResourceResponse.status(Status.CONFLICT);
		}
		mDef.setMeasureDefId(0);
		mDef = measureDefinitionDao.create(mDef);
		if (mDef != null)
			return ResourceResponse.ok(mDef.getMeasureDefId());
		else
			return ResourceResponse.notModified();
	}

	@Override
	public MeasureDefinition readMeasureDefinition(int id) {
		return measureDefinitionDao.read(id);
	}

	@Override
	public ResourceResponse updateMeasureDefinition(int id,
			MeasureDefinition mDef) {
		MeasureDefinition oldDef = measureDefinitionDao.read(id);
		if (oldDef == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		if (!isMeasureDefValid(mDef)) {
			return ResourceResponse.notAcceptable();
		}
		List<MeasureDefinition> list = measureDefinitionDao.readByName(mDef
				.getMeasureName());
		if (!list.isEmpty()) {
			return ResourceResponse.status(Status.CONFLICT);
		}
		mDef.setMeasureDefId(id);
		measureDefinitionDao.update(mDef);
		return ResourceResponse.ok(mDef.getMeasureDefId());
	}

	@Override	
	public ResourceResponse deleteMeasureDefinition(int id) {
		MeasureDefinition mDef = measureDefinitionDao.read(id);
		if (mDef == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		} else {
			measureDefinitionDao.delete(mDef);
		}
		mDef = measureDefinitionDao.read(id);
		if (mDef == null) {
			return ResourceResponse.ok();
		} else {
			return ResourceResponse.notModified();
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
