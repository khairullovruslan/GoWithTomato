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
}
