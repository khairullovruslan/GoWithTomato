package org.tomato.gowithtomato.controller.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.BaseServlet;
import org.tomato.gowithtomato.service.AuthService;

import java.io.IOException;


@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    private AuthService authService;
    @Override
    public void init() {
        super.init();
        authService = (AuthService) this.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        authService.logout(req, resp);
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
