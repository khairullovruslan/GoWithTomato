package org.tomato.gowithtomato.controller.auth;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;
import org.tomato.gowithtomato.exception.auth.RegistrationException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.util.UserUtil;

import java.io.IOException;
import java.util.Set;

@Slf4j
@WebServlet("/sign-up")
public class SignUpServlet extends BaseServlet {
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("templates/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserRegistrationDTO userRegistrationDto = UserUtil.getInstance().buildUserRegistrationDTO(req);
        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(userRegistrationDto);
        if (!violations.isEmpty()) {
            log.error("Пользователь ввел невалидные данные!");
            throw new RegistrationException(violations);
        }
        authService.registration(userRegistrationDto);
        resp.sendRedirect(req.getContextPath() + "/login");


    }
}
