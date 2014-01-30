package model.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DaoJpaImpl<T, PK extends Serializable> implements Dao<T, PK> {
	private final static Logger LOGGER = Logger.getLogger(DaoJpaImpl.class
			.getName());
	private static final String PERSISTENCE_UNIT_NAME = "FinalProject";

	protected Class<T> entityClass;
	private static EntityManagerFactory entityManagerFactory;

	static {
		entityManagerFactory = Persistence
				.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		LOGGER.log(Level.CONFIG, "static EntityManagerFactory created");
	}

	@SuppressWarnings("unchecked")
	public DaoJpaImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		entityClass = ((Class<T>) genericSuperclass.getActualTypeArguments()[0]);
	}

	@Override
	public T create(T t) {
		EntityManager entityManager = createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		entityManager.persist(t);
		tx.commit();
		closeConnections(entityManager);
		return t;
	}

	@Override
	public T read(PK id) {
		EntityManager entityManager = createEntityManager();
		T t = entityManager.find(entityClass, id);
		closeConnections(entityManager);
		return t;
	}

	@Override
	public T update(T t) {
		EntityManager entityManager = createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		T res = entityManager.merge(t);
		tx.commit();
		closeConnections(entityManager);
		return res;
	}

	@Override
	public void delete(T t) {
		EntityManager entityManager = createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		t = entityManager.merge(t);
		entityManager.remove(t);
		tx.commit();
		closeConnections(entityManager);
	}

	protected static EntityManager createEntityManager() {
		EntityManager em = entityManagerFactory.createEntityManager();
		return em;
	}

	protected static void closeConnections(EntityManager entityManager) {
		entityManager.close();
	}
}
