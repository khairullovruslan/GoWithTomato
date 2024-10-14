package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.entity.Trip;

public interface TripDAO extends CrudDao<Long, Trip> {
    Trip save(Trip trip, Long id);
}
