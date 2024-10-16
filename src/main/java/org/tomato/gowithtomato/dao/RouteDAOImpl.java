package org.tomato.gowithtomato.dao;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
public class RouteDAOImpl implements RouteDAO {
    private final static RouteDAOImpl INSTANCE = new RouteDAOImpl();
    private final PointDAOImpl pointDAO = PointDAOImpl.getInstance();
    private final UserDAOImpl userDAO = UserDAOImpl.getInstance();
    private final RouteAndPointsDaoImpl routeAndPointsDao = RouteAndPointsDaoImpl.getInstance();
    private final static String SAVE_SQL =
            """
                    INSERT INTO route(start_point_id, finish_point_id, distance, user_id) values (?, ?, ?, ?)
                    """;

    private final static String FIND_BY_USER_ID_SQL =
            """
                    SELECT  * FROM route where user_id = ?
                    """;
    private final static String FIND_BY_ID_SQL =
            """
                    SELECT  * FROM route where id = ?
                    """;


    private RouteDAOImpl() {
    }

    public static RouteDAOImpl getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    @Override
    public Optional<Route> findById(Long id) {
        @Cleanup var connection = connectionManager.get();
        return findById(connection, id);

    }

    public Optional<Route> findById(Connection connection, Long id) throws SQLException {
        var statement = connection.prepareStatement(FIND_BY_ID_SQL);
        statement.setLong(1, id);
        List<Route> routes = convertResultSetToList(connection, statement.executeQuery());
        return routes.size() == 1 ? Optional.ofNullable(routes.getFirst()) : Optional.empty();


    }

    @Override
    public List<Route> findAll() {
        return List.of();
    }

    @SneakyThrows
    @Override
    public Route save(Route route) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);

            Point startPoint = savePoint(connection, route.getDeparturePoint());
            Point finishPoint = savePoint(connection, route.getDestinationPoint());

            statement.setLong(1, startPoint.getId());
            statement.setLong(2, finishPoint.getId());
            statement.setDouble(3, 10000);
            statement.setLong(4, route.getOwner().getId());
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                route.setId(keys.getLong("id"));
                if (route.getOther() != null) {
                    routeAndPointsDao.saveAll(connection, route.getOther(), route.getId());
                }
            }
            connection.commit();
            return route;

        } catch (SQLException e) {
            connection.rollback();
            throw new DaoException();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Point savePoint(Connection connection, Point point) throws SQLException {
        try {
            return pointDAO.save(connection, point);
        } catch (PSQLException e) {
            if ("23505".equals(e.getSQLState())) {
                log.error("локация уже есть {}", point.getName());
                Optional<Point> existingPoint = pointDAO.findByLatLng(connection, point.getLat(), point.getLng());
                return existingPoint.orElseThrow(DaoException::new);
            } else {
                throw e;
            }
        }
    }


    @Override
    public Route update(Route entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    public List<Route> findByUser(User user) {
        try (var connection = connectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_USER_ID_SQL)) {
            statement.setLong(1, user.getId());
            return convertResultSetToList(connection, statement.executeQuery(), user);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Route> convertResultSetToList(Connection connection, ResultSet result, User user) throws SQLException {

        ArrayList<Route> currencies = new ArrayList<>();
        while (result.next()) {
            Optional<Point> start = pointDAO.findById(connection, result.getLong("start_point_id"));
            Optional<Point> finish = pointDAO.findById(connection, result.getLong("finish_point_id"));
            if (start.isEmpty() || finish.isEmpty()) {
                throw new DaoException();
            }
            List<Point> others = routeAndPointsDao.findByRouteId(connection, result.getLong("id"));
            currencies.add(Route
                    .builder()
                    .id(result.getLong("id"))
                    .departurePoint(start.get())
                    .destinationPoint(finish.get())
                    .owner(user)
                    .distance(result.getDouble("distance"))
                    .other(others)
                    .build());
        }
        return currencies;
    }

    private List<Route> convertResultSetToList(Connection connection, ResultSet result) throws SQLException {

        ArrayList<Route> currencies = new ArrayList<>();
        while (result.next()) {
            Optional<Point> start = pointDAO.findById(connection, result.getLong("start_point_id"));
            Optional<Point> finish = pointDAO.findById(connection, result.getLong("finish_point_id"));
            Optional<User> owner = userDAO.findById(connection, result.getLong("user_id"));
            if (start.isEmpty() || finish.isEmpty() || owner.isEmpty()) {
                throw new DaoException();
            }
            List<Point> others = routeAndPointsDao.findByRouteId(connection, result.getLong("id"));
            currencies.add(Route
                    .builder()
                    .id(result.getLong("id"))
                    .departurePoint(start.get())
                    .destinationPoint(finish.get())
                    .distance(result.getDouble("distance"))
                    .owner(owner.get())
                    .other(others)
                    .build());
        }
        return currencies;
    }
}
