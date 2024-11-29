package org.tomato.gowithtomato.controller.auth;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.UserRegistrationDto;
import org.tomato.gowithtomato.exception.auth.RegistrationException;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.util.UserRegistrationUtil;

import java.io.IOException;
import java.util.Set;

@Slf4j
@WebServlet("/sign-up")
public class SignUpServlet extends BaseServlet {
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        authService = (AuthService) this.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("templates/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserRegistrationDto userRegistrationDto = UserRegistrationUtil.getInstance().buildUserDTO(req);
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(userRegistrationDto);
        if (!violations.isEmpty()) {
            log.error("Пользователь ввел невалидные данные!");
            throw new RegistrationException(violations);
        }
        authService.registration(userRegistrationDto);
        resp.sendRedirect(req.getContextPath() + "/login");


    }
}
