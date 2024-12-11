package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.exception.db.UniqueSqlException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class UserDAO extends AbstractCrudDAO<Long, User> {
    public abstract Optional<User> findByLogin(String login) throws DaoException;

    public abstract Optional<String> getPasswordByLogin(String login);

    public abstract Optional<User> findByEmail(String email);

    public abstract void updatePhotoUrl(String url, Long userId);

    public abstract void updateUserPassword(String password, Long userId);

    public abstract Optional<User> getByTripId(Long tripId);

    public abstract Optional<User> getByRouteId(Long routeId);

    protected Optional<String> convertResultSetToPassword(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString("password"));
        }
        return Optional.empty();
    }

    protected List<User> convertResultSetToList(ResultSet result) throws SQLException {
        List<User> users = new ArrayList<>();
        while (result.next()) {
            users.add((mapper.mapRow(result)));
        }
        return users;
    }

    protected void handleSQLException(SQLException e) throws UniqueSqlException {
        if ("23505".equals(e.getSQLState())) {
            throw new UniqueSqlException("Ошибка уникальности: %s".formatted(e.getMessage()), e);
        }
    }
}
