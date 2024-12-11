package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ReviewDAO extends AbstractBaseDAO<Review> {
    public abstract boolean searchForUserInReviews(long tripId, long userId);

    public abstract Optional<Review> findByUserAndTripId(Long userId, Long tripId);

    public abstract List<Review> findByTripOwnerId(Long id);

    public abstract Review save(Review entity);

    protected List<Review> convertResultSetToList(ResultSet result) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        while (result.next()) {
            reviews.add(mapper.mapRow(result));
        }
        return reviews;
    }
}
