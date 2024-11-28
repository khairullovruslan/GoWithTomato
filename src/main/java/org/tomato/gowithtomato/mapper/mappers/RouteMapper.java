package org.tomato.gowithtomato.mapper.mappers;

import org.tomato.gowithtomato.dao.daoInterface.m2m.RouteAndPointsDao;
import org.tomato.gowithtomato.dao.impl.RouteAndPointsDaoImpl;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RouteMapper implements RowMapper<Route> {

    private final static RouteMapper INSTANCE = new RouteMapper();
    private final RouteAndPointsDao routeAndPointsDao;

    private RouteMapper() {
        routeAndPointsDao = RouteAndPointsDaoImpl.getInstance();
    }

    public static RouteMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Route mapRow(ResultSet resultSet) throws SQLException {
        Point startPoint = createPointFromResultSet(resultSet, "start_");
        Point finishPoint = createPointFromResultSet(resultSet, "finish_");

        User owner = User.builder()
                .id(resultSet.getLong("user_id"))
                .login(resultSet.getString("user_login"))
                .email(resultSet.getString("user_email"))
                .phoneNumber(resultSet.getString("user_phone_number"))
                .build();

        List<Point> others = routeAndPointsDao.findByRouteId(resultSet.getLong("route_id"));
        return Route.builder()
                .id(resultSet.getLong("route_id"))
                .departurePoint(startPoint)
                .destinationPoint(finishPoint)
                .distance(resultSet.getDouble("distance"))
                .owner(owner)
                .other(others)
                .build();

    }

    private Point createPointFromResultSet(ResultSet result, String prefix) throws SQLException {
        return Point.builder()
                .id(result.getLong(prefix + "id"))
                .lat(result.getDouble(prefix + "lat"))
                .lng(result.getDouble(prefix + "lng"))
                .name(result.getString(prefix + "name"))
                .country(result.getString(prefix + "country"))
                .state(result.getString(prefix + "state"))
                .osmValue(result.getString(prefix + "osm_value"))
                .build();
    }
}
