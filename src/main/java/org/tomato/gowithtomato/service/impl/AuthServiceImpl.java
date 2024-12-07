package org.tomato.gowithtomato.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;
import org.tomato.gowithtomato.exception.auth.WrongPasswordException;
import org.tomato.gowithtomato.factory.DaoFactory;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.mapper.UserMapper;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.util.PasswordUtil;

@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserDAO userDao;
    private final UserService userService;

    private final UserMapper userMapper;

    private final static AuthServiceImpl INSTANCE = new AuthServiceImpl();


    private AuthServiceImpl() {
        this.userService = ServiceFactory.getUserService();
        this.userDao = DaoFactory.getUserDAO();
        userMapper = UserMapper.getInstance();
    }

    public static AuthServiceImpl getInstance() {
        return INSTANCE;
    }

    public void registration(UserRegistrationDTO userDTO) {

        userDao.save(userMapper.convertUserRegistrationDTOToUser(userDTO));
        log.info("Пользователь успешно зарегистрирован");

    }

    public void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.removeAttribute("user");

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
    public void updateUser(HttpServletRequest req, UserDTO userDTO){
        req.getSession().setAttribute("user", userDTO);
    }
}
