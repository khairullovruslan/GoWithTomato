package org.tomato.gowithtomato.dao.daoInterface.base;

import org.tomato.gowithtomato.exception.db.DaoException;

import java.util.Optional;

/**
 * Абстрактный интерфейс CRUD для работы с сущностями
 *
 * @param <K> Тип идентификатора
 * @param <E> Тип сущности
 */
public abstract class AbstractCrudDAO<K, E> extends AbstractBaseDAO<E> {

    /**
     * Находит сущность по идентификатору
     *
     * @param id Идентификатор сущности
     * @return Объект Optional, содержащий найденную сущность, или empty, если сущность не найдена
     */
    public abstract Optional<E> findById(K id);

    /**
     * Сохраняет новую сущность в базе данных
     *
     * @param entity Сохраняемая сущность
     * @return Сохраненная сущность с проставленнывм id
     * @throws DaoException если произошла ошибка при сохранении сущности
     */
    public abstract E save(E entity) throws DaoException;

    /**
     * Обновляет существующую сущность в базе данных
     *
     * @param entity Обновляемая сущность
     * @return Обновленная сущность
     * @throws DaoException если произошла ошибка при обновлении сущности
     */
    public abstract E update(E entity) throws DaoException;

    /**
     * Удаляет сущность по идентификатору
     *
     * @param id Идентификатор удаляемой сущности
     */
    public abstract void delete(K id);
}
