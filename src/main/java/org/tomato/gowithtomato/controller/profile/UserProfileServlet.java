package org.tomato.gowithtomato.controller.profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.service.UserService;

import java.io.IOException;


@WebServlet("/profile")
public class UserProfileServlet extends BaseServlet {
    private UserService userService;

    @Override
    public void init() {
        super.init();
        userService = (UserService) this.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("u");
        if (name == null) {
            req.setAttribute("isOwner", true);
        } else {
            req.setAttribute("user", userService.findUserByLogin(name));
        }
        req.getRequestDispatcher("templates/profile.jsp").forward(req, resp);

    }
}
