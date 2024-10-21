package org.tomato.gowithtomato.dao;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.entity.*;
import org.tomato.gowithtomato.exception.db.DaoException;
import static org.tomato.gowithtomato.dao.query.TripQueries.*;
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



    public static TripDAOImpl getInstance() {
        return INSTANCE;
    }


    public Trip saveWithRouteId(Trip trip, Long id) {
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
            throw new DaoException("Не удалось получить id для новой поездки.");

        } catch (SQLException e) {
            throw new DaoException("Ошибка при сохранении поездки.", e);
        }
    }

    @SneakyThrows
    @Override
    public Optional<Trip> findById(Long id) {
        try (Connection connection = connectionManager.get()) {
            return findById(connection, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске поездки по id: " + id, e);
        }
    }

    public Optional<Trip> findById(Connection connection, Long id) throws SQLException {
        @Cleanup var statement = connection.prepareStatement(FIND_BY_ID_SQL);
       statement.setLong(1, id);
       List<Trip> trips = convertResultSetToList(connection, statement.executeQuery());
       return trips.size() == 1 ? Optional.ofNullable(trips.getFirst()) : Optional.empty();
    }

    @SneakyThrows
    public List<Trip> findAll() {
        try (Connection connection = connectionManager.get()) {
            return findAll(connection);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при получении всех поездок", e);
        }

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
    @SneakyThrows
    public List<Trip> findAllByFilter(Map<String, String> filter) {
        try (Connection connection = connectionManager.get()) {
            return findAllByFilter(connection, filter);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске с фильтром", e);
        }
    }


    public List<Trip> findAllByFilter(Connection connection, Map<String, String> filter) throws SQLException {
        String query = getQueryByFilter(filter);
        @Cleanup var statement = connection.prepareStatement(query);
        return convertResultSetToList(connection, statement.executeQuery());
    }
    @SneakyThrows
    public boolean addNewMember(Connection connection, Long tripId) {
        var statement = connection.prepareStatement(ADD_NEW_MEMBER_SQL);
        statement.setLong(1, tripId);
        int updated  = statement.executeUpdate();
        return updated > 0;
    }

    private List<Trip> convertResultSetToList(Connection connection, ResultSet result) throws SQLException {
        List<Trip> points = new ArrayList<>();
        while (result.next()) {
            points.add(createTripFromResultSet(connection, result));
        }
        return points;
    }
    private Trip createTripFromResultSet(Connection connection, ResultSet result) throws SQLException {
        Optional<User> owner = userDAO.findById(connection, result.getLong("user_id"));
        Optional<Route> route = routeDAO.findById(connection, result.getLong("route_id"));
        if (owner.isEmpty() || route.isEmpty()){
            throw  new DaoException("Ошибка при поиске owner || route для поездки");
        }
        return Trip.builder()
                .id(result.getLong("id"))
                .price(result.getBigDecimal("price"))
                .availableSeats(result.getInt("available_seats"))
                .tripDateTime(result.getTimestamp("trip_date_time").toLocalDateTime())
                .status(TripStatus.valueOf(result.getString("status")))
                .owner(owner.get())
                .route(route.get())
                .build();
    }



    private String getQueryByFilter(Map<String, String> filter){
        StringBuilder query = new StringBuilder(SQL_FILTER);
        List<String> conditions = new ArrayList<>();
        filter.forEach((key, value) -> {
            switch (key) {
                case "from" -> conditions.add("p1.name = '" + value + "'");
                case "to" -> conditions.add("p2.name = '" + value + "'");
                case "count" -> conditions.add("t.available_seats >= '" + value + "'");
                case "date" -> {
                    String formattedDate = LocalDateTime.parse(value)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    conditions.add("t.trip_date_time >= '" + formattedDate + "'");
                }
            }
        });

        if (!conditions.isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return query.toString();
    }


}
