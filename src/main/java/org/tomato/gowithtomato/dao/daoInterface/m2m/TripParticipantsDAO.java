package org.tomato.gowithtomato.dao.daoInterface.m2m;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.User;

import java.util.List;

public abstract class TripParticipantsDAO extends AbstractBaseDAO {
    public abstract List<User> findUsersByTripId(Long id);

    public abstract void save(Long tripId, Long userId);
}
