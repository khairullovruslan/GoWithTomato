package org.tomato.gowithtomato.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.UserService;

import java.io.IOException;


@WebServlet("/profile")
public class UserProfileServlet extends BaseServlet {
    private UserService userService;
    private RouteService routeService;
    @Override
    public void init() {
        super.init();
        routeService = RouteService.getInstance();
        userService = (UserService) this.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String name = req.getParameter("u");
        if (name == null){
            String login = (String) session.getAttribute("user");
            UserDTO user = userService.findUserByLogin(login);
            req.setAttribute("userData", user);
            req.setAttribute("isOwner", true);
        }

        else{
            req.setAttribute("userData",  userService.findUserByLogin(name));
        }
        req.getRequestDispatcher("templates/profile.jsp").forward(req, resp);

    }
}
