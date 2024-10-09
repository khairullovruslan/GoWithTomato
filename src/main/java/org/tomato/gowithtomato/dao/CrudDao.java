package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.exception.DaoException;
import org.tomato.gowithtomato.util.ConnectionManager;

import java.util.List;
import java.util.Optional;

public interface CrudDao<K, E> {
    Optional<E> findById(K id);

    List<E> findAll();

    E save(E entity) throws DaoException;

    E update(E entity);

    void delete(K id);

    final ConnectionManager connectionManager = ConnectionManager.getInstance();

}