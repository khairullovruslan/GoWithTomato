package org.tomato.gowithtomato.service;


import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserEditDTO;

import java.util.Optional;

public interface UserService {
    UserDTO findUserByLogin(String login);

    String getPasswordByLogin(String login);

    UserDTO findByEmail(String email);

    UserDTO edit(UserDTO user, UserEditDTO userEditDTO);

    UserDTO updatePhotoUrl(UserDTO origUser, String url, Long userId);

    void delete(Long id);

    Optional<UserDTO> getByTripId(Long tripId);

    Optional<UserDTO> getByRouteId(Long routeId);
}
