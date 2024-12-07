package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;

public class UserUtil {
    private static final UserUtil INSTANCE = new UserUtil();

    private UserUtil() {
    }

    public static UserUtil getInstance() {
        return INSTANCE;
    }

    public UserRegistrationDTO buildUserRegistrationDTO(HttpServletRequest req) {
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        return UserRegistrationDTO.builder()
                .login(login)
                .password(pwd)
                .phoneNumber(phone)
                .email(email)
                .build();
    }

}
