package org.tomato.gowithtomato.controller.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;

import java.io.IOException;

/*
Выход из аккаунта
*/
@Slf4j
@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
    }


    /*
    Выход пользователя
   */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Процесс выхода из аккаунта");
        authService.logout(req);
        resp.sendRedirect("%s/login".formatted(req.getContextPath()));
    }
}
