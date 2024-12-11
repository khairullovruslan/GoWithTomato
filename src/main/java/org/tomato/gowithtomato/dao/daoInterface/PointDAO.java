package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.db.UniqueSqlException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PointDAO extends AbstractBaseDAO<Point> {
    public abstract Point save(Point entity);

    public abstract Optional<Point> findByLatLng(Double lat, Double lng);

    public abstract Optional<Point> findById(Long id);

    protected Optional<Point> convertResultSetToOptional(ResultSet result) throws SQLException {
        List<Point> points = convertResultSetToList(result);
        return points.isEmpty() ? Optional.empty() : Optional.of(points.getFirst());
    }

    protected List<Point> convertResultSetToList(ResultSet result) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (result.next()) {
            points.add(mapper.mapRow(result));
        }
        return points;
    }

    protected void handleSQLException(SQLException e) throws UniqueSqlException {
        if ("23505".equals(e.getSQLState())) {
            throw new UniqueSqlException("Ошибка уникальности", e);
        }
    }
}
