package org.tomato.gowithtomato.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/routes")
public class RoutesServlet extends BaseServlet {
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
//        String login = (String) session.getAttribute("user");
//        UserDTO user = userService.findUserByLogin(login);
                UserDTO user = userService.findUserByLogin("ruslan");
        List<RouteDTO> routeDTOList = routeService.findByUser(user);
        req.setAttribute("routeList", routeDTOList);
        req.getRequestDispatcher("/templates/routes.jsp").forward(req, resp);
    }
}
