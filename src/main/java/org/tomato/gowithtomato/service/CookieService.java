package org.tomato.gowithtomato.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;


public class CookieService {
    private final static CookieService INSTANCE = new CookieService();
    private final UserService userService;

    private CookieService() {
        userService = UserService.getInstance();
    }

    public static CookieService getInstance() {
        return INSTANCE;
    }


    public void setCookie(HttpServletResponse resp, String login) {
        Cookie usernameCookie = new Cookie("username", login);
        usernameCookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(usernameCookie);
    }

    public void deleteCookie(HttpServletResponse resp) {
        Cookie usernameCookie = new Cookie("username", null);
        usernameCookie.setMaxAge(0);
        resp.addCookie(usernameCookie);
    }

    public UserDTO findUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                return userService.findUserByLogin(cookie.getValue());
            }
        }
        throw new UserNotFoundException();
    }
}
