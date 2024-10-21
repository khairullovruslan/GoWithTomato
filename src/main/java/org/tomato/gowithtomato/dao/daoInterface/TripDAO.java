package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.CrudDao;
import org.tomato.gowithtomato.entity.Trip;

public interface TripDAO extends CrudDao<Long, Trip> {
    Trip saveWithRouteId(Trip trip, Long id);
}
