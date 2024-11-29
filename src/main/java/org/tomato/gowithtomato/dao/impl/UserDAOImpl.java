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

/**
 * Реализация DAO для работы с сущностями типа User.
 */
public class UserDAOImpl extends UserDAO {
    private final static UserDAOImpl INSTANCE = new UserDAOImpl();

    private UserDAOImpl() {
        mapper = UserMapper.getInstance();
    }

    /**
     * Получает единственный экземпляр UserDAOImpl.
     *
     * @return единственный экземпляр данного DAO.
     */
    public static UserDAOImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Optional<User>, содержащий найденного пользователя, или empty, если пользователь не найден.
     */
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


    /**
     * Сохраняет нового пользователя в базе данных.
     *
     * @param user пользователь для сохранения.
     * @return сохраненный пользователь с присвоенным идентификатором.
     * @throws DaoException если возникла ошибка при сохранении пользователя.
     */
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

    /**
     * Обновляет информацию о пользователе.
     *
     * @param entity пользователь, данные которого следует обновить.
     * @return обновленный пользователь, или null, если пользователь не найден.
     */
    @Override
    public User update(User entity) {
        // Метод еще не реализован.
        return null;
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     */
    @Override
    public void delete(Long id) {
        // Метод еще не реализован.
    }

    /**
     * Находит пользователя по его логину.
     *
     * @param login логин пользователя.
     * @return Optional<User>, содержащий найденного пользователя, или пустой, если пользователь не найден.
     */
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

    /**
     * Преобразует ResultSet в список пользователей.
     *
     * @param result Результат выполнения SQL-запроса.
     * @return Список пользователей.
     * @throws SQLException если произошла ошибка при обработке ResultSet.
     */
    private List<User> convertResultSetToList(ResultSet result) throws SQLException {
        List<User> users = new ArrayList<>();
        while (result.next()) {
            users.add((mapper.mapRow(result)));
        }
        return users;
    }

    /**
     * Обрабатывает SQL-исключения.
     *
     * @param e Исключение SQL.
     * @throws UniqueSqlException если ошибка связана с уникальностью записи.
     */
    private void handleSQLException(SQLException e) throws UniqueSqlException {
        if ("23505".equals(e.getSQLState())) {
            throw new UniqueSqlException("Ошибка уникальности: " + e.getMessage(), e);
        }
    }
}
