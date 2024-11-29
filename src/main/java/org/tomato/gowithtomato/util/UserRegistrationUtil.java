package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.gowithtomato.dto.UserRegistrationDto;

public class UserRegistrationUtil {
    private static final UserRegistrationUtil INSTANCE = new UserRegistrationUtil();

    private UserRegistrationUtil() {
    }

    public static UserRegistrationUtil getInstance() {
        return INSTANCE;
    }

    public UserRegistrationDto buildUserDTO(HttpServletRequest req) {
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        return UserRegistrationDto.builder()
                .login(login)
                .password(pwd)
                .phoneNumber(phone)
                .email(email)
                .build();
    }
}
