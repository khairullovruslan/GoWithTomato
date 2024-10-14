package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dao.RouteDAOImpl;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

public class RouteService {
    private final static RouteService INSTANCE = new RouteService();
    private final RouteDAOImpl routeDAO;
    private final RouteMapper routeMapper;
    private final UserMapper userMapper;
    private RouteService(){
        routeDAO = RouteDAOImpl.getInstance();
        routeMapper = RouteMapper.getInstance();
        userMapper = UserMapper.getInstance();
    }

    public static RouteService getInstance() {
        return INSTANCE;
    }
    public void saveRoute(Route route){
        routeDAO.save(route);
    }
    public List<RouteDTO> findByUser(UserDTO user){
        List<Route> routes =  routeDAO.findByUser(userMapper.convertDTOToUser(user));
        return routes.stream().map(routeMapper::convertRouteToDTO).toList();
    }

    public Optional<RouteDTO> findById(Long id){
        Optional<Route> route =  routeDAO.findById(id);
        return route.map(routeMapper::convertRouteToDTO).or(Optional::empty);
    }
}
