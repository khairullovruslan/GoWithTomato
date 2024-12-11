package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;

import java.util.List;

public interface RouteService {
    void saveRoute(RouteDTO route, Long ownerId);

    RouteDTO findById(Long id);

    List<RouteDTO> findByUserWithPagination(UserDTO user, int i);

    long getCountPage(UserDTO user);
}
