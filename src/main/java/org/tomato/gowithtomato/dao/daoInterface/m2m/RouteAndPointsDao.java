package org.tomato.gowithtomato.dao.daoInterface.m2m;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class RouteAndPointsDao extends AbstractBaseDAO {
    public abstract void saveAll(List<Point> pointList, Long routeId) throws SQLException;

    public abstract List<Point> findByRouteId(Long id) throws SQLException;

    protected void executeBatchWithHandling(PreparedStatement statement) {
        try {
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при выполнении пакетной вставки.", e);
        }
    }

    protected List<Point> convertResultSetToList(ResultSet result) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (result.next()) {
            points.add(createPointFromResultSet(result));
        }
        return points;
    }

    protected Point createPointFromResultSet(ResultSet result) throws SQLException {
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
