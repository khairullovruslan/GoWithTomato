package org.tomato.gowithtomato.dao.daoInterface.base;

import org.tomato.gowithtomato.exception.db.DaoException;

import java.util.Optional;

public abstract class AbstractCrudDAO<K, E> extends AbstractBaseDAO<E> {

    public abstract Optional<E> findById(K id);

    public abstract E save(E entity) throws DaoException;

    public abstract void update(E entity) throws DaoException;

    public abstract void delete(K id);
}
