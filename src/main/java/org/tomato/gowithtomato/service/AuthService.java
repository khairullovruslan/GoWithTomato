package org.tomato.gowithtomato.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.WrongPasswordException;
import org.tomato.gowithtomato.util.PasswordUtil;

@Slf4j
public class AuthService {
    private final UserDAOImpl userDao;
    private final  ModelMapper mapper;
    private final PasswordUtil passwordUtil;
    private final SessionAndCookieService sessionAndCookieService;
    private final UserService userService;


    public AuthService() {
        this.userService = UserService.getInstance();
        this.sessionAndCookieService = SessionAndCookieService.getInstance();
        this.passwordUtil = PasswordUtil.getInstance();
        this.userDao = UserDAOImpl.getInstance();
        this.mapper = new ModelMapper();
    }
    public void registration(UserDTO userDTO) {
        String password = passwordUtil.hashPassword(userDTO.getPassword());
        userDTO.setPassword(password);
        userDao.save(mapper.map(userDTO, User.class));
        log.info("Пользователь успешно зарегестрирован");

    }

    public void logout(HttpServletRequest req,  HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        sessionAndCookieService.deleteCookie(resp);
        log.info("Пользователь успешно вышел");
    }

    public void login(HttpServletRequest req, HttpServletResponse res) {
        String login = req.getParameter("login");
        UserDTO user = userService.findUserByLogin(login);
        String pwd = req.getParameter("pwd");
        if (passwordUtil.checkPassword(pwd, user.getPassword())) {
            sessionAndCookieService.createSession(req, login);
            sessionAndCookieService.setCookie(res, login);
            log.info("Пользователь успешно вошел в систему");
        }
        else {
            throw new WrongPasswordException();
        }


    }
}
