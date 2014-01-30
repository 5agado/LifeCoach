package model.dao;

import java.io.Serializable;

public interface Dao<T, PK extends Serializable> {
	T create(T t);

	T read(PK id);

	T update(T t);

	void delete(T t);
}
