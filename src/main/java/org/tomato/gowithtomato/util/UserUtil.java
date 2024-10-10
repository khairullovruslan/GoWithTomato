package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.gowithtomato.dto.UserDTO;

public class UserUtil {
    private static final  UserUtil INSTANCE = new UserUtil();
    private UserUtil(){
    }

    public static UserUtil getInstance() {
        return INSTANCE;
    }
    public UserDTO buildUserDTO(HttpServletRequest req){
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        return UserDTO
                .builder()
                .password(pwd)
                .login(login)
                .phoneNumber(phone)
                .email(email)
                .build();
    }
}
