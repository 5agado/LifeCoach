package resources.ws;

import java.util.Date;
import java.util.List;

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
import model.MeasureDefinition;
import model.Person;
import model.ResourceResponse;
import model.ResourceResponse.Status;
import model.dao.MeasureDao;
import model.dao.MeasureDefinitionDao;
import model.dao.PersonDao;
import util.Utils;

//
@WebService(targetNamespace = "http://www.sagado.edu/soi/project",
	endpointInterface = "resources.ws.IMeasureResource")
public class MeasureResourceImpl implements IMeasureResource {
	private final PersonDao personDao = PersonDao.getInstance();
	private final MeasureDao measureDao = MeasureDao.getInstance();
	private final MeasureDefinitionDao measureDefinitionDao = MeasureDefinitionDao
			.getInstance();

	@Override
	public List<Measure> readAllMeasures( int personId,
			 String measureName, String before, String after) {
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

	@Override
	public ResourceResponse createMeasure(Measure measure,
			 int personId,
			 String measureName) {
		Person p = personDao.read(personId);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		if (!isMeasureValueValid(measure)) {
			return ResourceResponse.notAcceptable();
		}
		measure.setMeasureDefinition(list.get(0));
		measure.setPerson(p);
		measure.setMeasureId(0);
		if (measure.getTimestamp() == null) {
			measure.setTimestamp(new Date());
		}
		measure = measureDao.create(measure);
		if (measure != null)
			return ResourceResponse.ok(measure.getMeasureId());
		else
			return ResourceResponse.notModified();
	}

	@Override
	public Measure readMeasure( int personId,
			 String measureName,
			 int measureId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return null;
		}
		Measure m = measureDao.readByCostraints(personId, list.get(0)
				.getMeasureDefId(), measureId);
		return m;
	}

	@Override
	public ResourceResponse updateMeasure(Measure measure,
			 int personId,
			 String measureName,
			 int measureId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		Measure m = measureDao.readByCostraints(personId, list.get(0)
				.getMeasureDefId(), measureId);
		if (m == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		Person p = personDao.read(personId);
		if (p == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		if (!isMeasureValueValid(measure)) {
			return ResourceResponse.notAcceptable();
		}
		measure.setMeasureId(measureId);
		measure.setPerson(p);
		measure.setMeasureDefinition(list.get(0));
		if (measure.getTimestamp() == null) {
			measure.setTimestamp(new Date());
		}
		measureDao.update(measure);
		return ResourceResponse.ok(measure.getMeasureId());
	}

	@Override
	public ResourceResponse deleteMeasure( int personId,
			 String measureName,
			 int measureId) {
		List<MeasureDefinition> list = measureDefinitionDao
				.readByName(measureName);
		if (list.isEmpty()) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		Measure m = measureDao.readByCostraints(personId, list.get(0)
				.getMeasureDefId(), measureId);
		if (m == null) {
			return ResourceResponse.status(Status.NOT_FOUND);
		}
		measureDao.delete(m);
		m = measureDao.read(measureId);
		if (m == null) {
			return ResourceResponse.ok();
		} else
			return ResourceResponse.notModified();
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
