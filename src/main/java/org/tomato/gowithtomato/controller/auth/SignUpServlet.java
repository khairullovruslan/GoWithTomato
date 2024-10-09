package org.tomato.gowithtomato.controller.auth;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import org.tomato.gowithtomato.controller.BaseServlet;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.RegistrationException;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.util.UserUtil;

import java.io.IOException;
import java.util.Set;

@WebServlet("/sign-up")
public class SignUpServlet extends BaseServlet {
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        authService = (AuthService) this.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        processTemplate("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDTO userDTO = UserUtil.getInstance().buildUserDTO(req);
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            throw new RegistrationException(violations);
        }
        authService.registration(userDTO);
        resp.sendRedirect("/home");
    }
}
