package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.UserMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.TripQueries.CANCEL_TRIP_BY_USER_ID_SQL;
import static org.tomato.gowithtomato.dao.query.UserQueries.*;

public class UserDAOImpl extends UserDAO {
    private final static UserDAOImpl INSTANCE = new UserDAOImpl();

    private UserDAOImpl() {
        mapper = UserMapper.getInstance();
    }

    public static UserDAOImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            List<User> users = convertResultSetToList(result);
            return users.size() == 1 ? Optional.of(users.getFirst()) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя с id: %s ".formatted(id), e);
        }

    }


    @Override
    public User save(User user) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Ошибка при сохранении пользователя: ни одна строка не была изменена.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                    return user;
                } else {
                    throw new DaoException("Ошибка при получении идентификатора нового пользователя.");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
            throw new DaoException("Ошибка при сохранении пользователя.", e);
        }
    }


    @Override
    public void update(User entity) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL)) {

            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPhoneNumber());
            statement.setString(3, entity.getEmail());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при обновления пользователя по логину: %s".formatted(entity.getLogin()), e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID_SQL);
                 PreparedStatement statementForCancelTrip = connection.prepareStatement(CANCEL_TRIP_BY_USER_ID_SQL)) {

                statementForCancelTrip.setLong(1, id);
                statementForCancelTrip.executeUpdate();
                statement.setLong(1, id);
                statement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new DaoException("Ошибка при удаления пользователя по id: %s".formatted(id), e);
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка при удаления пользователя по id: %s".formatted(id), e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN_SQL)) {

            statement.setString(1, login);
            List<User> users = convertResultSetToList(statement.executeQuery());
            return users.size() == 1 ? Optional.of(users.getFirst()) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по логину: %s".formatted(login), e);
        }
    }

    @Override
    public Optional<String> getPasswordByLogin(String login) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PASSWORD_BY_LOGIN)) {

            statement.setString(1, login);
            return convertResultSetToPassword(statement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по логину: %s".formatted(login), e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {

            statement.setString(1, email);
            List<User> users = convertResultSetToList(statement.executeQuery());
            return users.size() == 1 ? Optional.of(users.getFirst()) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по email: %s".formatted(email), e);
        }
    }

    @Override
    public void updatePhotoUrl(String url, Long userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PHOTO_SQL)) {

            statement.setString(1, url);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при обновления автарки пользователя по id - %s".formatted(userId), e);
        }
    }

    @Override
    public void updateUserPassword(String password, Long userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_PASSWORD_SQL)) {

            statement.setString(1, password);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при обновления пароли пользователя по id - %s".formatted(userId), e);
        }
    }

    @Override
    public Optional<User> getByTripId(Long tripId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_TRIP_ID_SQL)) {

            statement.setLong(1, tripId);
            List<User> users = convertResultSetToList(statement.executeQuery());
            return users.size() == 1 ? Optional.of(users.getFirst()) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по trip id: %s".formatted(tripId), e);
        }
    }

    @Override
    public Optional<User> getByRouteId(Long routeId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ROUTE_ID_SQL)) {

            statement.setLong(1, routeId);
            List<User> users = convertResultSetToList(statement.executeQuery());
            return users.size() == 1 ? Optional.of(users.getFirst()) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по route id: %s".formatted(routeId), e);
        }
    }

}
