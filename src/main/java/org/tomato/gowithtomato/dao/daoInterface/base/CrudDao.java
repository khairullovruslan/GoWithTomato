package org.tomato.gowithtomato.dao.daoInterface.base;

import org.tomato.gowithtomato.exception.DaoException;

import java.util.Optional;

public interface CrudDao<K, E>  extends BaseDAO{
    Optional<E> findById(K id);

    E save(E entity) throws DaoException;

    E update(E entity);

    void delete(K id);

}