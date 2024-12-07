package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.util.List;
import java.util.Map;

public abstract class TripDAO extends AbstractCrudDAO<Long, Trip> {
    public abstract Trip saveWithRouteId(Trip trip, Long routeId) throws DaoException;

    public abstract long getCountByUserId(Long id);


    public abstract List<Trip> findAll();

    public abstract List<Trip> findAllByFilter(Map<String, String> filter);

    public abstract Long getCountPage(Map<String, String> filter);

    public abstract void cancelTrip(Long id);
}
