package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.DaoException;
import org.tomato.gowithtomato.exception.UniqueException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    private final String SAVE_SQL = "insert into users(login, password, email, phone_number)" +
            "values (?, ? , ?, ?)";

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User save(User user) throws DaoException{
        try (var connection = connectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getLong("id"));
                return user;
            }
            throw new DaoException();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UniqueException();
        }
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
