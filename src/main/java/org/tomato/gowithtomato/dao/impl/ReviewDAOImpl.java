package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.ReviewDAO;
import org.tomato.gowithtomato.entity.Review;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.mappers.ReviewMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.ReviewQueries.*;

/**
 * Реализация интерфейса ReviewDAO для управления отзывами.
 */
public class ReviewDAOImpl extends ReviewDAO {
    private static final ReviewDAOImpl INSTANCE = new ReviewDAOImpl();

    private ReviewDAOImpl() {
        mapper = ReviewMapper.getInstance();
    }

    /**
     * Получает экземпляр синглтона ReviewDAOImpl.
     *
     * @return экземпляр синглтона
     */
    public static ReviewDAOImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Находит отзыв по заданному ID.
     *
     * @param id ID отзыва
     * @return объект отзыва, если найдено, иначе пустой Optional
     */
    @Override
    public Optional<Review> findById(Long id) {
        return Optional.empty(); // Реализация может быть добавлена при необходимости
    }

    /**
     * Находит отзыв по ID пользователя и ID поездки.
     *
     * @param userId ID пользователя
     * @param tripId ID поездки
     * @return объект отзыва или пустой Optional, если отзыв не найден
     * @throws DaoException если произошла ошибка при доступе к базе данных
     */
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

    /**
     * Сохраняет новый отзыв.
     *
     * @param entity объект отзыва для сохранения
     * @return сохраненный объект отзыва с присвоенным ID
     * @throws DaoException если произошла ошибка при сохранении отзыва
     */
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

    @Override
    public Review update(Review entity) {
        return null; // Реализация может быть добавлена при необходимости
    }

    @Override
    public void delete(Long id) {
        // Реализация удаления отзыва может быть добавлена при необходимости
    }

    /**
     * Проверяет, существует ли отзыв пользователя по данному tripId.
     *
     * @param tripId ID поездки
     * @param userId ID пользователя
     * @return true, если отзыв существует, иначе false
     * @throws DaoException если произошла ошибка при поиске отзыва
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

    /**
     * Преобразует ResultSet в список отзывов.
     *
     * @param result ResultSet для преобразования
     * @return список отзывов
     * @throws SQLException если произошла ошибка при доступе к ResultSet
     */
    private List<Review> convertResultSetToList(ResultSet result) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        while (result.next()) {
            reviews.add(mapper.mapRow(result));
        }
        return reviews;
    }
}
