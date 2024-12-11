package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.PointDAO;
import org.tomato.gowithtomato.dao.daoInterface.m2m.RouteAndPointsDao;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.exception.db.UniqueSqlException;
import org.tomato.gowithtomato.factory.DaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.tomato.gowithtomato.dao.query.m2m.RouteAndPointsQueries.FIND_BY_ID_SQL;
import static org.tomato.gowithtomato.dao.query.m2m.RouteAndPointsQueries.ROUTE_INTERMEDIATE_INSERT_SQL;

public class RouteAndPointsDaoImpl extends RouteAndPointsDao {
    private static final RouteAndPointsDaoImpl INSTANCE = new RouteAndPointsDaoImpl();
    private final PointDAO pointDAO;

    private RouteAndPointsDaoImpl() {
        pointDAO = DaoFactory.getPointDAO();
    }

    public static RouteAndPointsDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void saveAll(List<Point> pointList, Long routeId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ROUTE_INTERMEDIATE_INSERT_SQL)) {
            int sequence = 1;

            for (Point point : pointList) {
                Point savePoint = saveOrFindPoint(point);
                addRouteIntermediatePoint(statement, routeId, savePoint.getId(), sequence++);
            }
            executeBatchWithHandling(statement);
        }
    }


    @Override
    public List<Point> findByRouteId(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            return convertResultSetToList(result);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске точек по id маршрута: %s".formatted(id), e);
        }
    }

    private void addRouteIntermediatePoint(PreparedStatement statement, Long routeId, Long pointId, int sequence) throws SQLException {
        statement.setLong(1, routeId);
        statement.setLong(2, pointId);
        statement.setInt(3, sequence);
        statement.addBatch();
    }

    private Point saveOrFindPoint(Point point) {
        try {
            return pointDAO.save(point);
        } catch (UniqueSqlException e) {
            return pointDAO.findByLatLng(point.getLat(), point.getLng())
                    .orElseThrow(() -> new DaoException("Не удалось сохранить точку и не удалось найти её по координатам.", e));
        }
    }

}
