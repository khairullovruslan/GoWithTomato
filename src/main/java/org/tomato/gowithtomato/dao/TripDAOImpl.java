package org.tomato.gowithtomato.dao;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.entity.*;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TripDAOImpl implements TripDAO {
    private final static TripDAOImpl INSTANCE = new TripDAOImpl();
    private final UserDAOImpl userDAO;
    private final RouteDAOImpl routeDAO;

    private TripDAOImpl(){
        userDAO = UserDAOImpl.getInstance();
        routeDAO = RouteDAOImpl.getInstance();
    }
    private final static String  SAVE_SQL =
            """
                 INSERT  INTO trip(USER_ID, ROUTE_ID, TRIP_DATE_TIME, AVAILABLE_SEATS, PRICE, STATUS)
                 VALUES (?, ?, ?, ?, ?, ?)
            """;
    private final static String FIND_ALL_SQL =
            """
            SELECT * from trip
            """;

    private final static String FIND_BY_ID_SQL = " SELECT * from trip where id = ?";

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
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public Optional<Trip> findById(Long id) {
        @Cleanup var connection = connectionManager.get();
        return findById(connection, id);
    }

    public Optional<Trip> findById(Connection connection, Long id) throws SQLException {
       var statement = connection.prepareStatement(FIND_BY_ID_SQL);
       statement.setLong(1, id);
       List<Trip> trips = convertResultSetToList(connection, statement.executeQuery());
       return trips.size() == 1 ? Optional.ofNullable(trips.getFirst()) : Optional.empty();
    }

    @SneakyThrows
    @Override
    public List<Trip> findAll() {
        @Cleanup Connection connection = connectionManager.get();
        return findAll(connection);

    }
    public List<Trip> findAll(Connection connection) throws SQLException {
        var statement = connection.prepareStatement(FIND_ALL_SQL);
        var result = statement.executeQuery();
        return convertResultSetToList(connection, result);
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

    private List<Trip> convertResultSetToList(Connection connection, ResultSet result) throws SQLException {
        List<Trip> points = new ArrayList<>();
        while (result.next()) {
            Optional<User> owner = userDAO.findById(connection, result.getLong("user_id"));
            Optional<Route> route = routeDAO.findById(connection, result.getLong("route_id"));
            if (owner.isEmpty() || route.isEmpty()){
                throw  new DaoException();
            }
            points.add(Trip.builder()
                    .id(result.getLong("id"))
                    .price(result.getBigDecimal("price"))
                    .availableSeats(result.getInt("available_seats"))
                    .tripDateTime(result.getTimestamp("trip_date_time").toLocalDateTime())
                    .status(TripStatus.valueOf(result.getString("status")))
                    .owner(owner.get())
                    .route(route.get())
                    .build());

        }
        return points;
    }

    @SneakyThrows
    public List<Trip> findAllByFilter(Map<String, String> filter) {
        @Cleanup var connection = connectionManager.get();
        return findAllByFilter(connection, filter);
    }


    public List<Trip> findAllByFilter(Connection connection, Map<String, String> filter) throws SQLException {
       String query = getQueryByFilter(filter);
       var statement = connection.prepareStatement(query);
       return convertResultSetToList(connection, statement.executeQuery());
    }

    private String getQueryByFilter(Map<String, String> filter){
        StringBuilder query = new StringBuilder(
                """
                SELECT t.*
                FROM trip t
                         JOIN route r ON t.route_id = r.id
                         JOIN Point p1 ON r.start_point_id = p1.id
                         JOIN Point p2 ON r.finish_point_id = p2.id
                """);
        if (!filter.isEmpty()) query.append(" WHERE ");
        String[] s = new String[filter.keySet().size()];
        int count = 0;
        for (String key : filter.keySet()){
            switch (key) {
                case "from" -> s[count++] = String.format("p1.name = '%s'", filter.get(key));
                case "to" -> s[count++] = String.format("p2.name = '%s'", filter.get(key));
                case "count" -> s[count++] = String.format("t.available_seats >= '%s'", filter.get(key));
                case "date" -> {
                    s[count++] = String.format("t.trip_date_time >= '%s'", LocalDateTime.parse(filter.get(key)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        }
        query.append(String.join(" and ", s));
        return query.toString();
    }
}
