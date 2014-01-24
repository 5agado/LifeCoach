package model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import model.Goal;
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
	    List<Measure> list = entityManager.createNamedQuery("Measure.findByPerson", Measure.class)
	    		.setParameter("pId", personId).getResultList();
	    closeConnections(entityManager);
	    return list;
	}

	public List<Measure> readAllByPersonAndDefinition(int personId, int measureDefinitionId) {
		EntityManager entityManager = createEntityManager();
	    List<Measure> list = entityManager.createNamedQuery("Measure.findByPersonAndDefinition", Measure.class)
	    		.setParameter("pId", personId).setParameter("mDefId", measureDefinitionId).
	    		getResultList();
	    closeConnections(entityManager);
	    return list;
	}
	
	public List<Measure> readPersonProfile(int personId, String profileType) {
		EntityManager entityManager = createEntityManager();
		String query = "SELECT m "
		+ "FROM Measure m "
		+ "WHERE m.person.personId = :pId AND m.measureDefinition.measureDefId IN ("
		+ "SELECT mf.measureDefId "
		+ "FROM MeasureDefinition mf "
		+ "WHERE mf.profileType = :profileType "
		+ ") "
		+ "GROUP BY m.measureDefinition "
		+ "ORDER BY m.timestamp DESC ";
	    List<Measure> list = entityManager.createQuery(query, Measure.class)
	    		.setParameter("pId", personId).setParameter("profileType", profileType).
	    		getResultList();
	    closeConnections(entityManager);
	    return list;
	}
}
