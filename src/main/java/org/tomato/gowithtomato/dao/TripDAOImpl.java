package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

public class TripDAOImpl implements TripDAO {
    private final static TripDAOImpl INSTANCE = new TripDAOImpl();

    private TripDAOImpl(){

    }
    private final static String  SAVE_SQL =
            """
                 INSERT  INTO trip(USER_ID, ROUTE_ID, TRIP_DATE_TIME, AVAILABLE_SEATS, PRICE, STATUS)
                 VALUES (?, ?, ?, ?, ?, ?)
            """;

    public static TripDAOImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Trip save(Trip trip, Long id) {
        try (var connection = connectionManager.get();
        var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            statement.setLong(1, trip.getOwner().getId());
            statement.setLong(2, id);
            statement.setObject(3, trip.getTripDateTime());
            statement.setInt(4, trip.getAvailableSeats());
            statement.setBigDecimal(5, trip.getPrice());
            statement.setObject(6, TripStatus.available, Types.OTHER);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                trip.setId(keys.getLong("id"));
                return trip;
            }
            throw new DaoException();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Trip> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Trip> findAll() {
        return List.of();
    }

    @Override
    public Trip save(Trip entity) throws DaoException {
        return null;
    }

    @Override
    public Trip update(Trip entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
