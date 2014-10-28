package model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import model.MeasureDefinition;

public class MeasureDefinitionDao extends
		DaoJpaImpl<MeasureDefinition, Integer> {
	private static final MeasureDefinitionDao INSTANCE = new MeasureDefinitionDao();

	public static MeasureDefinitionDao getInstance() {
		return INSTANCE;
	}

	public List<MeasureDefinition> readAll() {
		EntityManager entityManager = createEntityManager();
		List<MeasureDefinition> list = entityManager.createNamedQuery(
				"MeasureDefinition.findAll", MeasureDefinition.class)
				.getResultList();
		closeConnections(entityManager);
		return list;
	}

	public List<MeasureDefinition> readByName(String measureName) {
		EntityManager entityManager = createEntityManager();
		List<MeasureDefinition> list = entityManager
				.createNamedQuery("MeasureDefinition.findByName",
						MeasureDefinition.class)
				.setParameter("measureName", measureName).getResultList();

		closeConnections(entityManager);
		return list;
	}

	public List<MeasureDefinition> readByProfileType(String profileType) {
		EntityManager entityManager = createEntityManager();
		List<MeasureDefinition> list = entityManager
				.createNamedQuery("MeasureDefinition.findByProfileType",
						MeasureDefinition.class)
				.setParameter("profileType", profileType).getResultList();

		closeConnections(entityManager);
		return list;
	}
}
