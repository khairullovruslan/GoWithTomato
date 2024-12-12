package org.tomato.gowithtomato.service.impl;

import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.exception.db.RoutNotFoundException;
import org.tomato.gowithtomato.factory.DaoFactory;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.mapper.UserMapper;
import org.tomato.gowithtomato.service.RouteService;

import java.util.List;
import java.util.Optional;

public class RouteServiceImpl implements RouteService {
    private final static RouteServiceImpl INSTANCE = new RouteServiceImpl();
    private final RouteDAO routeDAO;
    private final RouteMapper routeMapper;
    private final UserMapper userMapper;

    private RouteServiceImpl() {
        routeDAO = DaoFactory.getRouteDAO();
        routeMapper = RouteMapper.getInstance();
        userMapper = UserMapper.getInstance();
    }

    public static RouteServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override

    public void saveRoute(RouteDTO route, Long ownerId) {
        routeDAO.save(routeMapper.convertDTOToRoute(route), ownerId);
    }

    @Override

    public RouteDTO findById(Long id) {
        Optional<Route> route = routeDAO.findById(id);
        if (route.isPresent()) {
            return routeMapper.convertRouteToDTO(route.get());
        }
        throw new RoutNotFoundException();
    }

    @Override

    public List<RouteDTO> findByUserWithPagination(UserDTO user, int page) {
        List<Route> routes = routeDAO.findByUserWithPagination(userMapper.convertDTOToUser(user), page);
        return routes.stream().map(routeMapper::convertRouteToDTO).toList();
    }

    @Override

    public long getCountPage(UserDTO user) {
        return routeDAO.getCountPage(userMapper.convertDTOToUser(user));
    }
}
