package org.tomato.gowithtomato.service;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserLoginDTO;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;

public interface AuthService {

    void registration(UserRegistrationDTO userDTO);


    void logout(HttpServletRequest req);

    void login(UserLoginDTO userLoginDTO, HttpServletRequest req);


    boolean authorizationCheck(HttpServletRequest req);

    UserDTO getUser(HttpServletRequest req);

    void updateUser(HttpServletRequest req, UserDTO userDTO);

    void updateUserPassword(String password, Long userId);
}
