package org.tomato.gowithtomato.dao;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.tomato.gowithtomato.dao.daoInterface.TripParticipantsDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripParticipantsDAOImpl implements TripParticipantsDAO {
    private final static TripParticipantsDAOImpl INSTANCE = new TripParticipantsDAOImpl();
    private final UserDAOImpl userDAO;
    private TripParticipantsDAOImpl(){
        userDAO = UserDAOImpl.getInstance();
    }
    private final static String FIND_USERS_BY_TRIP_ID_SQL = """
            select * from trip_participants where trip_id = ?
            """;

    public static TripParticipantsDAOImpl getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public List<User> findUsersByTripId(Long id) {
        @Cleanup var connection = connectionManager.get();
        return findUsersByTripId(connection, id);
    }
    public List<User> findUsersByTripId(Connection connection, Long id) throws SQLException {
        var statement = connection.prepareStatement(FIND_USERS_BY_TRIP_ID_SQL);
        statement.setLong(1, id);
        return convertResultSetToUserList(connection, statement.executeQuery());
    }
    private List<User> convertResultSetToUserList(Connection connection, ResultSet result) throws SQLException {
        List<User> users = new ArrayList<>();
        while (result.next()){
            Long id = result.getLong("user_id");
            Optional<User> user = userDAO.findById(connection, id);
            user.ifPresent(users::add);
        }
        return users;

    }

    @Override
    public Optional findById(Object id) {
        return Optional.empty();
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Object save(Object entity) throws DaoException {
        return null;
    }

    @Override
    public Object update(Object entity) {
        return null;
    }

    @Override
    public void delete(Object id) {

    }
}
