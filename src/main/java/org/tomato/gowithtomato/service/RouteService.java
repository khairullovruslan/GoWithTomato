package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dao.impl.RouteDAOImpl;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.exception.db.RoutNotFoundException;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

public class RouteService {
    private final static RouteService INSTANCE = new RouteService();
    private final RouteDAOImpl routeDAO;
    private final RouteMapper routeMapper;
    private final UserMapper userMapper;

    private RouteService() {
        routeDAO = RouteDAOImpl.getInstance();
        routeMapper = RouteMapper.getInstance();
        userMapper = UserMapper.getInstance();
    }

    public static RouteService getInstance() {
        return INSTANCE;
    }

    public void saveRoute(Route route) {
        routeDAO.save(route);
    }


    public RouteDTO findById(Long id) {
        Optional<Route> route = routeDAO.findById(id);
        if (route.isPresent()) {
            return routeMapper.convertRouteToDTO(route.get());
        }
        throw new RoutNotFoundException();
    }

    public List<RouteDTO> findByUserWithPagination(UserDTO user, int i) {
        List<Route> routes = routeDAO.findByUserWithPagination(userMapper.convertDTOToUser(user), i);
        return routes.stream().map(routeMapper::convertRouteToDTO).toList();
    }

    public long getCountPage(UserDTO user) {
        return routeDAO.getCountPage(userMapper.convertDTOToUser(user));
    }
}
