package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dao.daoInterface.PointDao;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.DaoException;
import org.tomato.gowithtomato.exception.UniqueSqlException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.PointQueries.*;

public class PointDAOImpl implements PointDao {
    private static final PointDAOImpl INSTANCE = new PointDAOImpl();

    private PointDAOImpl() {}

    public static PointDAOImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Point save(Connection connection, Point entity) throws DaoException, SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, entity.getLat());
            statement.setDouble(2, entity.getLng());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getCountry());
            statement.setString(5, entity.getState());
            statement.setString(6, entity.getOsmValue());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DaoException("Не удалось сохранить точку.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }
            return entity;
        } catch (SQLException e) {
            handleSQLException(e);
            throw new DaoException("Ошибка при сохранении точки.", e);
        }
    }

    @Override
    public Optional<Point> findByLatLng(Connection connection, Double lat, Double lng) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_COORDINATE_SQL)) {
            statement.setDouble(1, lat);
            statement.setDouble(2, lng);
            ResultSet result = statement.executeQuery();
            return convertResultSetToOptional(result);
        }
    }

    @Override
    public Optional<Point> findById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            return convertResultSetToOptional(result);
        }
    }

    private Optional<Point> convertResultSetToOptional(ResultSet result) throws SQLException {
        List<Point> points = convertResultSetToList(result);
        return points.isEmpty() ? Optional.empty() : Optional.of(points.getFirst());
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
                .name(result.getString("name"))
                .state(result.getString("state"))
                .osmValue(result.getString("osm_value"))
                .country(result.getString("country"))
                .build();
    }

    private void handleSQLException(SQLException e) throws UniqueSqlException {
        if ("23505".equals(e.getSQLState())) {
            throw new UniqueSqlException();
        }
    }
}
