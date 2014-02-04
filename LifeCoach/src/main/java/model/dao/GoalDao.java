package model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import model.Goal;

public class GoalDao extends DaoJpaImpl<Goal, Integer> {
	private static final GoalDao INSTANCE = new GoalDao();

	public static GoalDao getInstance() {
		return INSTANCE;
	}

	public List<Goal> readAll() {
		EntityManager entityManager = createEntityManager();
		List<Goal> list = entityManager.createNamedQuery("Goal.findAll",
				Goal.class).getResultList();
		closeConnections(entityManager);
		return list;
	}

	public Goal readByConstraints(int personId, int measureDefinitionId,
			int goalId) {
		EntityManager entityManager = createEntityManager();
		List<Goal> list = entityManager
				.createNamedQuery("Goal.findByPersonDefinitionAndId",
						Goal.class).setParameter("pId", personId)
				.setParameter("mDefId", measureDefinitionId)
				.setParameter("gId", goalId).getResultList();
		closeConnections(entityManager);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public List<Goal> readAllByPerson(int personId) {
		EntityManager entityManager = createEntityManager();
		List<Goal> list = entityManager
				.createNamedQuery("Goal.findByPerson", Goal.class)
				.setParameter("pId", personId).getResultList();
		closeConnections(entityManager);
		return list;
	}

	public List<Goal> readAllByPersonAndDefinition(int personId,
			int measureDefinitionId) {
		EntityManager entityManager = createEntityManager();
		List<Goal> list = entityManager
				.createNamedQuery("Goal.findByPersonAndDefinition", Goal.class)
				.setParameter("pId", personId)
				.setParameter("mDefId", measureDefinitionId).getResultList();
		closeConnections(entityManager);
		return list;
	}

	public List<Goal> readPersonProfileGoals(int personId, String profileType) {
		EntityManager entityManager = createEntityManager();
		List<Goal> list = new ArrayList<Goal>();
		if (profileType.isEmpty()) {
			String query = "SELECT g "
					+ "FROM Goal g "
					+ "WHERE g.person.personId = :pId AND g.measureDefinition.measureDefId IN ("
					+ "SELECT mf.measureDefId " + "FROM MeasureDefinition mf )";
			list = entityManager.createQuery(query, Goal.class)
					.setParameter("pId", personId).getResultList();
		} else {
			String query = "SELECT g "
					+ "FROM Goal g "
					+ "WHERE g.person.personId = :pId AND g.measureDefinition.measureDefId IN ("
					+ "SELECT mf.measureDefId " + "FROM MeasureDefinition mf "
					+ "WHERE mf.profileType = :profileType " + ") ";
			list = entityManager.createQuery(query, Goal.class)
					.setParameter("pId", personId)
					.setParameter("profileType", profileType).getResultList();
		}
		closeConnections(entityManager);
		return list;
	}

	public void deletePersonProfileGoals(int personId, String profileType) {
		EntityManager entityManager = createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		if (profileType.isEmpty()) {
			String query = "DELETE " + "FROM Goal g "
					+ "WHERE g.person.personId = :pId";
			entityManager.createQuery(query).setParameter("pId", personId)
					.executeUpdate();
		} else {
			String query = "DELETE "
					+ "FROM Goal g "
					+ "WHERE g.person.personId = :pId AND g.measureDefinition.measureDefId IN ("
					+ "SELECT mf.measureDefId " + "FROM MeasureDefinition mf "
					+ "WHERE mf.profileType = :profileType " + ") ";
			entityManager.createQuery(query).setParameter("pId", personId)
					.setParameter("profileType", profileType).executeUpdate();
		}
		tx.commit();
		closeConnections(entityManager);
	}
}
