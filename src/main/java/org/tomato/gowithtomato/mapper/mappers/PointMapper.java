package org.tomato.gowithtomato.mapper.mappers;

import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PointMapper implements RowMapper<Point> {
    private final static PointMapper INSTANCE = new PointMapper();

    private PointMapper() {
    }

    public static PointMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Point mapRow(ResultSet result) throws SQLException {

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


}
