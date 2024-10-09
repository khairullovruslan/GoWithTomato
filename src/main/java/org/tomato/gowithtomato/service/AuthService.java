package org.tomato.gowithtomato.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.util.PasswordUtil;

import java.io.IOException;

@Slf4j
public class AuthService {
    private final UserDAOImpl userDao;
    private final  ModelMapper mapper;
    private final PasswordUtil passwordUtil;
    private final SessionAndCookieService sessionAndCookieService;


    public AuthService() {
        this.sessionAndCookieService = SessionAndCookieService.getInstance();
        this.passwordUtil = PasswordUtil.getInstance();
        this.userDao = UserDAOImpl.getInstance();
        this.mapper = new ModelMapper();
    }
    public void registration(UserDTO userDTO, HttpServletRequest request, HttpServletResponse response){
        String password = passwordUtil.hashPassword(userDTO.getPassword());
        userDTO.setPassword(password);
        userDao.save(mapper.map(userDTO, User.class));
        sessionAndCookieService.createSession(request, userDTO.getLogin());
        sessionAndCookieService.setCookie(response, userDTO.getLogin());
        log.info("Пользователь успешно зарегестрирован");

    }

    public void logout(HttpServletRequest req,  HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        sessionAndCookieService.deleteCookie(resp);
        log.info("Пользователь успешно вышел");
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
