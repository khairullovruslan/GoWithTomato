package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.entity.Point;

public class PointMapper {
    private static final PointMapper INSTANCE = new PointMapper();
    private PointMapper(){
    }

    public static PointMapper getInstance() {
        return INSTANCE;
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
