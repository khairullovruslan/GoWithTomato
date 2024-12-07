package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.entity.Route;

import java.util.List;

public interface RouteService {
    void saveRoute(Route route);

    RouteDTO findById(Long id);

    List<RouteDTO> findByUserWithPagination(UserDTO user, int i);

    long getCountPage(UserDTO user);
}
