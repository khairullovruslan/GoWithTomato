package org.tomato.gowithtomato.controller.mapper;

import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteMapper {
    private static RouteMapper routeMapper = new RouteMapper();
    private RouteMapper(){

    }

    public static RouteMapper getInstance() {
        return routeMapper;
    }
    public Route convertDTOToRoute(RouteDTO routeDTO){
        PointMapper pointMapper = PointMapper.getInstance();
        Route route = Route
                .builder()
                .departurePoint(pointMapper.convertDTOToPoint(routeDTO.getStart()))
                .destinationPoint(pointMapper.convertDTOToPoint(routeDTO.getFinish()))
                .build();
        List<Point> pointList = new ArrayList<>();
        for(PointDTO pointDTO : routeDTO.getOthers()){
            pointList.add(
                    pointMapper.convertDTOToPoint(pointDTO));
        }
        route.setOther(pointList);
        return route;

    }

}
