package org.tomato.gowithtomato.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    Optional<T> findById(Long id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void delete(Long id);

}