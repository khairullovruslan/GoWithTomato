package org.tomato.gowithtomato.service;


import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserEditDTO;

public interface UserService {
    UserDTO findUserByLogin(String login);

    String getPasswordByLogin(String login);

    UserDTO findByEmail(String email);

    UserDTO edit(UserDTO user, UserEditDTO userEditDTO);
}
