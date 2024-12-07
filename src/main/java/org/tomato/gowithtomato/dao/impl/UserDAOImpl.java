package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.exception.db.UniqueSqlException;
import org.tomato.gowithtomato.mapper.UserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throw new DaoException("Ошибка при поиске пользователя с id: " + id, e);
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
            throw new DaoException("Ошибка при обновления пользователя по логину: " + entity.getLogin(), e);
        }
    }

    @Override
    public void delete(Long id) {
        // Метод еще не реализован.
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN_SQL)) {

            statement.setString(1, login);
            List<User> users = convertResultSetToList(statement.executeQuery());
            return users.size() == 1 ? Optional.of(users.getFirst()) : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по логину: " + login, e);
        }
    }

    @Override
    public Optional<String> getPasswordByLogin(String login) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PASSWORD_BY_LOGIN)) {

            statement.setString(1, login);
            return convertResultSetToPassword(statement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске пользователя по логину: " + login, e);
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
            throw new DaoException("Ошибка при поиске пользователя по email: " + email, e);
        }
    }

    private Optional<String> convertResultSetToPassword(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString("password"));
        }
        return Optional.empty();
    }

    private List<User> convertResultSetToList(ResultSet result) throws SQLException {
        List<User> users = new ArrayList<>();
        while (result.next()) {
            users.add((mapper.mapRow(result)));
        }
        return users;
    }

    private void handleSQLException(SQLException e) throws UniqueSqlException {
        if ("23505".equals(e.getSQLState())) {
            throw new UniqueSqlException("Ошибка уникальности: " + e.getMessage(), e);
        }
    }
}
