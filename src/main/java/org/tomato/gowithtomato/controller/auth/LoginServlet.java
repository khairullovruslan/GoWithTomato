package org.tomato.gowithtomato.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("templates/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        authService.login(req);
        resp.sendRedirect(req.getContextPath() + "/profile");

    }
}
