package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.ReviewDAO;
import org.tomato.gowithtomato.entity.Review;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.ReviewMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.ReviewQueries.*;

public class ReviewDAOImpl extends ReviewDAO {
    private static final ReviewDAOImpl INSTANCE = new ReviewDAOImpl();

    private ReviewDAOImpl() {
        mapper = ReviewMapper.getInstance();
    }

    public static ReviewDAOImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public Optional<Review> findByUserAndTripId(Long userId, Long tripId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_AND_TRIP_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, tripId);

            List<Review> reviews = convertResultSetToList(preparedStatement.executeQuery());
            return reviews.isEmpty() ? Optional.empty() : Optional.of(reviews.getFirst());

        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске отзыва по пользователю и поездке", e);
        }
    }

    @Override
    public List<Review> findByTripOwnerId(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_TRIP_OWNER_ID_SQL)) {
            preparedStatement.setLong(1, id);

            return convertResultSetToList(preparedStatement.executeQuery());

        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске отзыва по пользователю и поездке", e);
        }
    }


    @Override
    public Review save(Review entity) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getOwner().getId());
            preparedStatement.setLong(2, entity.getTrip().getId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setLong(4, entity.getRating());
            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getLong("id"));
                return entity;
            }
            throw new DaoException("Ошибка при получении ID сохраненного отзыва");

        } catch (SQLException e) {
            throw new DaoException("Ошибка при сохранении отзыва", e);
        }
    }

    /*
    поиск отзыва по юзеру
     */
    @Override
    public boolean searchForUserInReviews(long tripId, long userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_FOR_USER_IN_REVIEWS_SQL)) {
            statement.setLong(1, userId);
            statement.setLong(2, tripId);
            List<Review> reviews = convertResultSetToList(statement.executeQuery());

            if (reviews.size() > 1) {
                throw new DaoException("Некорректный поиск: найдено больше одного отзыва.");
            }
            return reviews.size() == 1;

        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске отзыва", e);
        }
    }


}
