package org.tomato.gowithtomato.dao;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


@Slf4j
public class RouteDAOImpl implements RouteDAO {
    private final static RouteDAOImpl INSTANCE = new RouteDAOImpl();
    private final PointDAOImpl pointDAO = PointDAOImpl.getInstance();
    private final RouteAndPointsDaoImpl routeAndPointsDao = RouteAndPointsDaoImpl.getInstance();
    private final static String SAVE_SQL =
            """
            INSERT INTO route(start_point_id, finish_point_id, distance) values (?, ?, ?)
            """;


    private RouteDAOImpl() {
    }

    public static RouteDAOImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Route> findById(Long id) {
        return Optional.empty();
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
}
