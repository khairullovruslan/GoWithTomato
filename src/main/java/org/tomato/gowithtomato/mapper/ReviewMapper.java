package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.entity.Review;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.factory.DaoFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReviewMapper implements RowMapper<Review> {
    private final static ReviewMapper INSTANCE = new ReviewMapper();

    private final TripDAO tripDAO;
    private final UserMapper userMapper;
    private final UserDAO userDAO;
    private final TripMapper tripMapper;

    private ReviewMapper() {
        userMapper = UserMapper.getInstance();
        tripDAO = DaoFactory.getTripDAO();
        userDAO = DaoFactory.getUserDAO();
        tripMapper = TripMapper.getInstance();
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

    public Review convertDTOToReview(ReviewDTO reviewDTO) {
        return Review
                .builder()
                .rating(reviewDTO.getRating())
                .description(reviewDTO.getDescription())
                .owner(userMapper.convertDTOToUser(reviewDTO.getOwner()))
                .trip(tripMapper.convertDTOToTrip(reviewDTO.getTrip()))
                .build();
    }

    public ReviewDTO convertReviewToDto(Review review) {
        return ReviewDTO
                .builder()
                .rating(review.getRating())
                .description(review.getDescription())
                .owner(userMapper.convertUserToDTO(review.getOwner()))
                .trip(tripMapper.convertTripToDTO(review.getTrip()))
                .build();
    }

}
