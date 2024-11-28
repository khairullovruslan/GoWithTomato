package org.tomato.gowithtomato.mapper.mappers;

import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.dao.impl.TripDAOImpl;
import org.tomato.gowithtomato.dao.impl.UserDAOImpl;
import org.tomato.gowithtomato.entity.Review;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReviewMapper implements RowMapper<Review> {
    private final static ReviewMapper INSTANCE = new ReviewMapper();

    private final TripDAO tripDAO;
    private final UserDAO userDAO;

    private ReviewMapper() {
        tripDAO = TripDAOImpl.getInstance();
        userDAO = UserDAOImpl.getInstance();
    }

    public static ReviewMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Review mapRow(ResultSet resultSet) throws SQLException {
        Optional<Trip> tripOptional = tripDAO.findById(resultSet.getLong("trip_id"));
        if (tripOptional.isEmpty()) {
            throw new DaoException("Поездка не найдена");
        }
        Optional<User> userOptional = userDAO.findById(resultSet.getLong("user_id"));
        if (userOptional.isEmpty()) {
            throw new DaoException("Пользователь не найден");
        }
        return Review
                .builder()
                .owner(userOptional.get())
                .trip(tripOptional.get())
                .description(resultSet.getString("description"))
                .rating(resultSet.getInt("rating"))
                .build();
    }

}
