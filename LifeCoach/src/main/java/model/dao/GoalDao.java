package model.dao;

import java.util.List;

import javax.persistence.EntityManager;

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

	public List<Goal> readAllByPersonAndDefinition(int personId, int measureDefinitionId) {
		EntityManager entityManager = createEntityManager();
	    List<Goal> list = entityManager.createNamedQuery("Goal.findByPersonAndDefinition", Goal.class)
	    		.setParameter("pId", personId).setParameter("mDefId", measureDefinitionId).
	    		getResultList();
	    closeConnections(entityManager);
	    return list;
	}
}
