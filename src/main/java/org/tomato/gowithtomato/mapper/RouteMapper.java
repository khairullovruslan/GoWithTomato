package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteMapper {
    private static final RouteMapper routeMapper = new RouteMapper();
    private final PointMapper pointMapper;
    private final UserMapper userMapper;
    private RouteMapper(){
        this.userMapper = UserMapper.getInstance();
        pointMapper = PointMapper.getInstance();
    }

    public static RouteMapper getInstance() {
        return routeMapper;
    }
    public Route convertDTOToRoute(RouteDTO routeDTO){
        Route route = Route
                .builder()
                .owner(userMapper.convertDTOToUser(routeDTO.getOwner()))
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

    public RouteDTO convertRouteToDTO(Route route){
        RouteDTO routeDTO = RouteDTO
                .builder()
                .id(route.getId())
                .owner(userMapper.convertUserToDTO(route.getOwner()))
                .start(pointMapper.convertPointToDTO(route.getDeparturePoint()))
                .finish(pointMapper.convertPointToDTO(route.getDestinationPoint()))
                .distance(route.getDistance())
                .build();
        List<PointDTO> pointList = new ArrayList<>();
        for(Point point : route.getOther()){
            pointList.add(
                    pointMapper.convertPointToDTO(point));
        }
        routeDTO.setOthers(pointList);
        return routeDTO;
    }

}
