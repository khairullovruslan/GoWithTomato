package org.tomato.gowithtomato.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dao.impl.UserDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.dto.UserRegistrationDto;
import org.tomato.gowithtomato.exception.auth.WrongPasswordException;
import org.tomato.gowithtomato.mapper.UserMapper;
import org.tomato.gowithtomato.util.PasswordUtil;

@Slf4j
public class AuthService {
    private final UserDAOImpl userDao;
    private final UserService userService;

    private final UserMapper userMapper;

    private final static AuthService INSTANCE = new AuthService();


    private AuthService() {
        this.userService = UserService.getInstance();
        this.userDao = UserDAOImpl.getInstance();
        userMapper = UserMapper.getInstance();
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public void registration(UserRegistrationDto userDTO) {

        userDao.save(userMapper.convertUserRegistrationDTOToUser(userDTO));
        log.info("Пользователь успешно зарегистрирован");

    }

    public void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();

        log.info("Пользователь успешно вышел");
    }

    public void login(HttpServletRequest req) {
        String login = req.getParameter("login");
        String password = userService.getPasswordByLogin(login);
        String pwd = req.getParameter("pwd");

        if (PasswordUtil.checkPassword(pwd, password)) {
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("user", userService.findUserByLogin(login));
            log.info("Пользователь успешно вошел в систему");
        } else {
            throw new WrongPasswordException();
        }
    }

    public boolean authorizationCheck(HttpServletRequest req) {
        return req.getSession().getAttribute("user") != null;
    }

    public UserDTO getUser(HttpServletRequest req) {
        return (UserDTO) req.getSession().getAttribute("user");
    }
}
