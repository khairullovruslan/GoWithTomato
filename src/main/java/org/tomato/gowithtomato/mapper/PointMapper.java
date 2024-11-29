package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.entity.Point;

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

    public Point convertDTOToPoint(PointDTO pointDTO){
        return Point
                .builder()
                .name(pointDTO.getName())
                .lat(pointDTO.getCoordPoint().getLat())
                .lng(pointDTO.getCoordPoint().getLng())
                .osmValue(pointDTO.getOsmValue())
                .country(pointDTO.getCountry())
                .state(pointDTO.getState())
                .build();
    }
    public PointDTO convertPointToDTO(Point point){
        return PointDTO
                .builder()
                .name(point.getName())
                .osmValue(point.getOsmValue())
                .country(point.getCountry())
                .state(point.getState())
                .coordPoint(PointDTO.CoordPoint
                        .builder()
                        .lng(point.getLng())
                        .lat(point.getLat())
                        .build())
                .build();
    }


}
