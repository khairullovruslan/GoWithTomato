package org.tomato.gowithtomato.dao;

import org.tomato.gowithtomato.dao.daoInterface.PointDao;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PointDAOImpl implements PointDao {
    private static final PointDAOImpl INSTANCE = new PointDAOImpl();

    private PointDAOImpl() {}

    private static final String SAVE_SQL =
        """
        INSERT INTO point(lat, lng, name, country, state, osm_value) VALUES (?, ?, ?, ?, ?, ?)  
        ON CONFLICT (lat, lng) DO UPDATE SET name = excluded.name, country = excluded.country, state = excluded.state, osm_value = excluded.osm_value  
        RETURNING id
        """;

    private static final String FIND_BY_COORDINATE_SQL =
        """
        SELECT * from point WHERE lat = ? AND lng = ?
        """;

    public static PointDAOImpl getInstance() {
        return INSTANCE;
    }

    public Point save(Connection connection, Point entity) throws DaoException, SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setDouble(1, entity.getLat());
            statement.setDouble(2, entity.getLng());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getCountry());
            statement.setString(5, entity.getState());
            statement.setString(6, entity.getOsmValue());

            try (ResultSet keys = statement.executeQuery()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }
            return entity;
        }
    }

    public Optional<Point> findByLatLng(Connection connection, Double lat, Double lng) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_COORDINATE_SQL)) {
            statement.setDouble(1, lat);
            statement.setDouble(2, lng);
            ResultSet result = statement.executeQuery();
            List<Point> points = convertResultSetToList(result);
            return points.isEmpty() ? Optional.empty() : Optional.of(points.get(0));
        }
    }

    private List<Point> convertResultSetToList(ResultSet result) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (result.next()) {
            points.add(Point.builder()
                    .id(result.getLong("id"))
                    .lng(result.getDouble("lng"))
                    .lat(result.getDouble("lat"))
                    .name(result.getString("name"))
                    .state(result.getString("state"))
                    .osmValue(result.getString("osm_value"))
                    .country(result.getString("country"))
                    .build());
        }
        return points;
    }
}
