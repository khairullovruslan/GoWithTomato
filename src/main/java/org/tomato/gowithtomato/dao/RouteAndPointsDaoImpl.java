package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dao.daoInterface.RouteAndPointsDao;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.DaoException;
import org.tomato.gowithtomato.exception.UniqueSqlException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.tomato.gowithtomato.dao.query.RouteAndPointsQueries.FIND_BY_ID_SQL;
import static org.tomato.gowithtomato.dao.query.RouteAndPointsQueries.ROUTE_INTERMEDIATE_INSERT_SQL;

public class RouteAndPointsDaoImpl implements RouteAndPointsDao {
    private static final RouteAndPointsDaoImpl INSTANCE = new RouteAndPointsDaoImpl();
    private final PointDAOImpl pointDAO;

    private RouteAndPointsDaoImpl() {
        pointDAO = PointDAOImpl.getInstance();
    }

    public static RouteAndPointsDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void saveAll(Connection connection, List<Point> pointList, Long routeId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ROUTE_INTERMEDIATE_INSERT_SQL)) {
            int sequence = 1;

            for (Point point : pointList) {
                Point savePoint = saveOrFindPoint(connection, point);
                addRouteIntermediatePoint(statement, routeId, savePoint.getId(), sequence++);
            }
            executeBatchWithHandling(statement);
        }
    }

    @Override
    public List<Point> findByRouteId(Connection connection, Long id) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            return convertResultSetToList(result);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске точек по id маршрута: " + id, e);
        }
    }

    private void addRouteIntermediatePoint(PreparedStatement statement, Long routeId, Long pointId, int sequence) throws SQLException {
        statement.setLong(1, routeId);
        statement.setLong(2, pointId);
        statement.setInt(3, sequence);
        statement.addBatch();
    }

    private Point saveOrFindPoint(Connection connection, Point point) throws SQLException {
        try {
            return pointDAO.save(connection, point);
        } catch (UniqueSqlException e) {
            return pointDAO.findByLatLng(connection, point.getLat(), point.getLng())
                    .orElseThrow(() -> new DaoException("Не удалось сохранить точку и не удалось найти её по координатам.", e));
        }
    }

    private void executeBatchWithHandling(PreparedStatement statement) {
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при выполнении пакетной вставки.", e);
        }
    }

    private List<Point> convertResultSetToList(ResultSet result) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (result.next()) {
            points.add(createPointFromResultSet(result));
        }
        return points;
    }

    private Point createPointFromResultSet(ResultSet result) throws SQLException {
        return Point.builder()
                .id(result.getLong("id"))
                .lng(result.getDouble("lng"))
                .lat(result.getDouble("lat"))
                .name(result.getString("title"))
                .state(result.getString("state"))
                .osmValue(result.getString("osm_value"))
                .country(result.getString("country"))
                .build();
    }
}

