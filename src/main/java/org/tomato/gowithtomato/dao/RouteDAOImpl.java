package org.tomato.gowithtomato.dao;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.DaoException;
import org.tomato.gowithtomato.exception.UniqueSqlException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.RouteQueries.*;


@Slf4j
public class RouteDAOImpl implements RouteDAO {
    private final static RouteDAOImpl INSTANCE = new RouteDAOImpl();
    private final PointDAOImpl pointDAO;
    private final RouteAndPointsDaoImpl routeAndPointsDao;


    private RouteDAOImpl() {
        pointDAO = PointDAOImpl.getInstance();
        routeAndPointsDao = RouteAndPointsDaoImpl.getInstance();

    }

    public static RouteDAOImpl getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    @Override
    public Optional<Route> findById(Long id) {
        try (Connection connection = connectionManager.get()) {
            return findById(connection, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске маршрута по id.", e);
        }
    }

    public Optional<Route> findById(Connection connection, Long id) throws SQLException {
        var statement = connection.prepareStatement(FIND_BY_ID_SQL);
        statement.setLong(1, id);
        List<Route> routes = convertResultSetToList(connection, statement.executeQuery());
        return routes.size() == 1 ? Optional.ofNullable(routes.getFirst()) : Optional.empty();
    }

    @Override
    public List<Route> findByUser(User user) {
        try (var connection = connectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_USER_ID_SQL)) {
            statement.setLong(1, user.getId());
            return convertResultSetToList(connection, statement.executeQuery());

        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске списка маршрутов пользователя", e);
        }
    }

    @SneakyThrows
    @Override
    public Route save(Route route) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionManager.get();
            connection.setAutoCommit(false);

            Long startPointId = saveOrFindPoint(connection, route.getDeparturePoint());
            Long finishPointId = saveOrFindPoint(connection, route.getDestinationPoint());

            long routeId = insertRoute(connection, startPointId, finishPointId, route.getOwner().getId());
            route.setId(routeId);

            if (route.getOther() != null) {
                routeAndPointsDao.saveAll(connection, route.getOther(), route.getId());
            } else {
                throw new SQLException("Не удалось получить  id для маршрута.");
            }
            connection.commit();
            return route;

        } catch (SQLException e) {
            connection.rollback();
            throw new DaoException("Ошибка при сохранении маршрута.", e);
        } finally {
            if (connection != null) {
                connection.close();
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

    private Long saveOrFindPoint(Connection connection, Point point) throws SQLException {
        try {
            return pointDAO.save(connection, point).getId();
        } catch (UniqueSqlException e) {
            Optional<Point> existingPoint = pointDAO.findByLatLng(connection, point.getLat(), point.getLng());
            return existingPoint.orElseThrow(() -> new DaoException("Ошибка точка не найдена и не может быть сохранена.")).getId();
        }
    }

    private long insertRoute(Connection connection, long startPointId, long finishPointId, long ownerId) throws SQLException {
        try (var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, startPointId);
            statement.setLong(2, finishPointId);
            statement.setDouble(3, 10000);
            statement.setLong(4, ownerId);

            statement.executeUpdate();

            try (var keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong("id");
                } else {
                    throw new SQLException("Не удалось id для маршрута.");
                }
            }
        }
    }


    private List<Route> convertResultSetToList(Connection connection, ResultSet result) throws SQLException {

        ArrayList<Route> currencies = new ArrayList<>();
        while (result.next()) {
            currencies.add(createRouteFromResultSet(connection, result));
        }
        return currencies;
    }

    private Route createRouteFromResultSet(Connection connection, ResultSet result) throws SQLException {
        Point startPoint = createPointFromResultSet(result, "start_");
        Point finishPoint = createPointFromResultSet(result, "finish_");

        User owner = User.builder()
                .id(result.getLong("user_id"))
                .login(result.getString("user_login"))
                .email(result.getString("user_email"))
                .phoneNumber(result.getString("user_phone_number"))
                .build();

        List<Point> others = routeAndPointsDao.findByRouteId(connection, result.getLong("id"));
        return Route.builder()
                .id(result.getLong("id"))
                .departurePoint(startPoint)
                .destinationPoint(finishPoint)
                .distance(result.getDouble("distance"))
                .owner(owner)
                .other(others)
                .build();
    }

    private Point createPointFromResultSet(ResultSet result, String prefix) throws SQLException {
        return Point.builder()
                .id(result.getLong(prefix + "id"))
                .lat(result.getDouble(prefix + "lat"))
                .lng(result.getDouble(prefix + "lng"))
                .name(result.getString(prefix + "name"))
                .country(result.getString(prefix + "country"))
                .state(result.getString(prefix + "state"))
                .osmValue(result.getString(prefix + "osm_value"))
                .build();
    }
}
