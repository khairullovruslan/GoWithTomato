package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.Review;

import java.util.List;
import java.util.Optional;

public abstract class ReviewDAO extends AbstractCrudDAO<Long, Review> {
    public abstract boolean searchForUserInReviews(long tripId, long userId);

    public abstract Optional<Review> findByUserAndTripId(Long userId, Long tripId);

    public abstract List<Review> findByTripOwnerId(Long id);
}
