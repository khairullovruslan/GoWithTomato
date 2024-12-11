package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dao.daoInterface.m2m.RouteAndPointsDao;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.factory.DaoFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteMapper implements RowMapper<Route> {

    private final static RouteMapper INSTANCE = new RouteMapper();
    private final RouteAndPointsDao routeAndPointsDao;
    private final PointMapper pointMapper;

    private RouteMapper() {
        routeAndPointsDao = DaoFactory.getRouteAndPointsDAO();
        pointMapper = PointMapper.getInstance();
    }

    public static RouteMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Route mapRow(ResultSet resultSet) throws SQLException {
        Point startPoint = createPointFromResultSet(resultSet, "start_");
        Point finishPoint = createPointFromResultSet(resultSet, "finish_");

        List<Point> others = routeAndPointsDao.findByRouteId(resultSet.getLong("route_id"));
        return Route.builder()
                .id(resultSet.getLong("route_id"))
                .departurePoint(startPoint)
                .destinationPoint(finishPoint)
                .distance(resultSet.getDouble("distance"))
                .other(others)
                .build();

    }

    private Point createPointFromResultSet(ResultSet result, String prefix) throws SQLException {
        return Point.builder()
                .id(result.getLong("%sid".formatted(prefix)))
                .lat(result.getDouble("%slat".formatted(prefix)))
                .lng(result.getDouble("%slng".formatted(prefix)))
                .name(result.getString("%sname".formatted(prefix)))
                .country(result.getString("%scountry".formatted(prefix)))
                .state(result.getString(1))
                .osmValue(result.getString("%sosm_value".formatted(prefix)))
                .build();
    }

    public Route convertDTOToRoute(RouteDTO routeDTO) {
        Route route = Route
                .builder()
                .departurePoint(pointMapper.convertDTOToPoint(routeDTO.getStart()))
                .destinationPoint(pointMapper.convertDTOToPoint(routeDTO.getFinish()))
                .distance(routeDTO.getDistance())
                .build();
        List<Point> pointList = new ArrayList<>();
        for (PointDTO pointDTO : routeDTO.getOthers()) {
            pointList.add(
                    pointMapper.convertDTOToPoint(pointDTO));
        }
        route.setOther(pointList);
        return route;
    }

    public RouteDTO convertRouteToDTO(Route route) {
        RouteDTO routeDTO = RouteDTO
                .builder()
                .id(route.getId())
                .start(pointMapper.convertPointToDTO(route.getDeparturePoint()))
                .finish(pointMapper.convertPointToDTO(route.getDestinationPoint()))
                .distance(route.getDistance())
                .build();
        List<PointDTO> pointList = new ArrayList<>();
        for (Point point : route.getOther()) {
            pointList.add(
                    pointMapper.convertPointToDTO(point));
        }
        routeDTO.setOthers(pointList);
        return routeDTO;
    }
}
