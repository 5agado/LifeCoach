package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import model.Measure;

public class MeasureDao extends DaoJpaImpl<Measure, Integer> {
	private static final MeasureDao INSTANCE = new MeasureDao();

	public static MeasureDao getInstance() {
		return INSTANCE;
	}

	public List<Measure> readAll() {
		EntityManager entityManager = createEntityManager();
		List<Measure> list = entityManager.createNamedQuery("Measure.findAll",
				Measure.class).getResultList();
		closeConnections(entityManager);
		return list;
	}

	public List<Measure> readAllByPerson(int personId) {
		EntityManager entityManager = createEntityManager();
		List<Measure> list = entityManager
				.createNamedQuery("Measure.findByPerson", Measure.class)
				.setParameter("pId", personId).getResultList();
		closeConnections(entityManager);
		return list;
	}

	public List<Measure> readAllByPersonAndDefinition(int personId,
			int measureDefinitionId) {
		EntityManager entityManager = createEntityManager();
		List<Measure> list = entityManager
				.createNamedQuery("Measure.findByPersonAndDefinition",
						Measure.class).setParameter("pId", personId)
				.setParameter("mDefId", measureDefinitionId).getResultList();
		closeConnections(entityManager);
		return list;
	}

	public List<Measure> readAllByPersonDefinitionAndDate(int personId,
			int measureDefinitionId, Date beforeDate, Date afterDate) {
		EntityManager entityManager = createEntityManager();
		String query;
		List<Measure> list = new ArrayList<Measure>();
		if (beforeDate == null) {
			query = "SELECT m FROM Measure m WHERE m.person.personId = :pId "
					+ "AND m.measureDefinition.measureDefId = :mDefId "
					+ "AND m.timestamp > :aDate "
					+ "ORDER BY m.timestamp DESC ";
			list = entityManager.createQuery(query, Measure.class)
					.setParameter("pId", personId)
					.setParameter("mDefId", measureDefinitionId)
					.setParameter("aDate", afterDate)
					.getResultList();
		} else if (afterDate == null) {
			query = "SELECT m FROM Measure m WHERE m.person.personId = :pId "
					+ "AND m.measureDefinition.measureDefId = :mDefId "
					+ "AND m.timestamp < :bDate "
					+ "ORDER BY m.timestamp DESC ";
			list = entityManager.createQuery(query, Measure.class)
					.setParameter("pId", personId)
					.setParameter("mDefId", measureDefinitionId)
					.setParameter("bDate", beforeDate)
					.getResultList();
		} else {
			query = "SELECT m FROM Measure m WHERE m.person.personId = :pId "
					+ "AND m.measureDefinition.measureDefId = :mDefId "
					+ "AND m.timestamp < :bDate AND m.timestamp > :aDate "
					+ "ORDER BY m.timestamp DESC ";
			list = entityManager.createQuery(query, Measure.class)
					.setParameter("pId", personId)
					.setParameter("mDefId", measureDefinitionId)
					.setParameter("bDate", beforeDate)
					.setParameter("aDate", afterDate)
					.getResultList();
		}
		closeConnections(entityManager);
		return list;
	}

	public List<Measure> readPersonProfile(int personId, String profileType) {
		EntityManager entityManager = createEntityManager();
		List<Measure> list = new ArrayList<Measure>();
		if (profileType.isEmpty()){
			String query = "SELECT m "
					+ "FROM Measure m "
					+ "WHERE m.person.personId = :pId AND m.measureDefinition.measureDefId IN ("
					+ "SELECT mf.measureDefId " + "FROM MeasureDefinition mf )"
					+ "GROUP BY m.measureDefinition "
					+ "ORDER BY m.timestamp DESC ";
			list = entityManager.createQuery(query, Measure.class)
					.setParameter("pId", personId).getResultList();
		}
		else {
			String query = "SELECT m "
					+ "FROM Measure m "
					+ "WHERE m.person.personId = :pId AND m.measureDefinition.measureDefId IN ("
					+ "SELECT mf.measureDefId " + "FROM MeasureDefinition mf "
					+ "WHERE mf.profileType = :profileType " + ") "
					+ "GROUP BY m.measureDefinition "
					+ "ORDER BY m.timestamp DESC ";
			list = entityManager.createQuery(query, Measure.class)
					.setParameter("pId", personId)
					.setParameter("profileType", profileType).getResultList();
		}
		closeConnections(entityManager);
		return list;
	}
}
