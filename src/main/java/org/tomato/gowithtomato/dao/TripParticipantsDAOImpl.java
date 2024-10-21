package org.tomato.gowithtomato.dao;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.tomato.gowithtomato.dao.daoInterface.TripParticipantsDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.DaoException;

import static org.tomato.gowithtomato.dao.query.TripParticipantsQueries.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TripParticipantsDAOImpl implements TripParticipantsDAO {
    private final static TripParticipantsDAOImpl INSTANCE = new TripParticipantsDAOImpl();
    private final UserDAOImpl userDAO;
    private final TripDAOImpl tripDAO;
    private TripParticipantsDAOImpl(){
        userDAO = UserDAOImpl.getInstance();
        tripDAO = TripDAOImpl.getInstance();
    }



    public static TripParticipantsDAOImpl getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public List<User> findUsersByTripId(Long id) {
        try (Connection connection = connectionManager.get()){
             return findUsersByTripId(connection, id);
        }
    }
    public List<User> findUsersByTripId(Connection connection, Long id) throws SQLException {
        @Cleanup var statement = connection.prepareStatement(FIND_USERS_BY_TRIP_ID_SQL);
        statement.setLong(1, id);
        return convertResultSetToUserList(connection, statement.executeQuery());
    }
    @SneakyThrows
    public void save(Long tripId, Long userId)  {
        @Cleanup var connection = connectionManager.get();
        connection.setAutoCommit(false);
        try {
            save(connection, tripId, userId);
        }
        catch (Exception e){
            connection.rollback();
            throw new DaoException("Ошибка при сохранении участника в trip: " + e.getMessage(), e);
        }

    }
    public void save(Connection connection, Long tripId, Long userId) throws SQLException {
        boolean updated = tripDAO.addNewMember(connection, tripId);
        if (!updated) throw new DaoException("Невозможно добавить нового участника: недостаточно мест.");
        var statement = connection.prepareStatement(SAVE_SQL);
        statement.setLong(1, tripId);
        statement.setLong(2, userId);
        statement.executeUpdate();
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


}
