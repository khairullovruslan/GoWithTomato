package org.tomato.gowithtomato.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.tomato.gowithtomato.dto.UserDTO;

public class SessionAndCookieService {
    private final static SessionAndCookieService INSTANCE = new SessionAndCookieService();
    private SessionAndCookieService(){
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
}
