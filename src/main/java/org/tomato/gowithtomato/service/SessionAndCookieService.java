package org.tomato.gowithtomato.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;


public class SessionAndCookieService {
    private final static SessionAndCookieService INSTANCE = new SessionAndCookieService();
    private final UserService userService;
    private SessionAndCookieService(){
        userService = UserService.getInstance();
    }
    public static SessionAndCookieService getInstance() {
        return INSTANCE;
    }
    public void createSession(HttpServletRequest req, String login){
        HttpSession session = req.getSession();
        session.setAttribute("user", login);

    }
    public void setCookie(HttpServletResponse resp, String login){
        Cookie usernameCookie = new Cookie("username", login);
        usernameCookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(usernameCookie);
    }
    public void deleteCookie(HttpServletResponse resp){
        Cookie usernameCookie = new Cookie("username", null);
        usernameCookie.setMaxAge(0);
        resp.addCookie(usernameCookie);
    }

    public UserDTO findUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
           throw new UserNotFoundException();
        }
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            if (cookie.getName().equals("username")){
                return userService.findUserByLogin(cookie.getValue());
            }
        }
        throw new UserNotFoundException();
    }
}
