package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.DaoException;
import org.tomato.gowithtomato.exception.UniqueException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO{
    private final static UserDAOImpl INSTANCE = new UserDAOImpl();
    private UserDAOImpl(){}

    private final String SAVE_SQL = "insert into users(login, password, email, phone_number)" +
            "values (?, ? , ?, ?)";

    private final String FIND_BY_LOGIN = "select * from users where login = ?";

    public static UserDAOImpl getInstance() {
        return INSTANCE;
    }

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

    @Override
    public Optional<User> findByLogin(String login) {
        try (var connection = connectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_LOGIN)){
            statement.setString(1, login);
            var result = statement.executeQuery();
            List<User> users = convertResultSetToList(result);
            return Optional.ofNullable(users.size() == 1 ? users.get(0) : null);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> convertResultSetToList(ResultSet result) throws SQLException {
        ArrayList<User> currencies = new ArrayList<>();
        while (result.next()) {
            currencies.add(User
                    .builder()
                    .id(result.getLong("id"))
                    .login(result.getString("login"))
                    .password(result.getString("password"))
                    .email(result.getString("email"))
                    .phoneNumber(result.getString("phone_number"))
                    .build());
        }
        return currencies;
    }
}
