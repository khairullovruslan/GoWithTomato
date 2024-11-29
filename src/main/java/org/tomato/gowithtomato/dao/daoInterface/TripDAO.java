package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.exception.db.DaoException;

/**
 * Абстрактный интерфейс для работы с сущностями типа Trip
 */
public abstract class TripDAO extends AbstractCrudDAO<Long, Trip> {

    /**
     * Сохраняет поездку с заданным идентификатором маршрута
     *
     * @param trip    Поездка, которую нужно сохранить
     * @param routeId Идентификатор маршрута, с которым связана поездка
     * @return Сохраненная поездка
     * @throws DaoException если произошла ошибка при сохранении поездки
     */
    public abstract Trip saveWithRouteId(Trip trip, Long routeId) throws DaoException;

    public abstract long getCountByUserId(Long id);
}
