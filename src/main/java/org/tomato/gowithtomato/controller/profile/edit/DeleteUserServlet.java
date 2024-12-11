package org.tomato.gowithtomato.controller.profile.edit;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.UserService;

import java.io.IOException;

/*
Удаление пользователя
 */
@Slf4j
@WebServlet("/profile/edit/delete")
public class DeleteUserServlet extends BaseServlet {

    private UserService userService;
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        userService = ServiceFactory.getUserService();
        authService = ServiceFactory.getAuthService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDTO userDTO = authService.getUser(req);
        log.info("Удаление пользователя - {}, {}", userDTO.getId(), userDTO.getLogin());
        userService.delete(userDTO.getId());
        resp.sendRedirect("%s/login".formatted(req.getContextPath()));
    }
}
