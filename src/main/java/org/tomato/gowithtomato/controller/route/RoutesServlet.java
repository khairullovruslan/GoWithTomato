package org.tomato.gowithtomato.controller.route;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.RouteService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/routes")
public class RoutesServlet extends BaseServlet {
    private RouteService routeService;

    @Override
    public void init() {
        super.init();
        routeService = RouteService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        if (page == null) {
            page = "1";
        }
        UserDTO user = (UserDTO) req.getAttribute("user");
        List<RouteDTO> routeDTOList = routeService.findByUserWithPagination(user, Integer.parseInt(page));
        req.setAttribute("routeList", routeDTOList);
        req.setAttribute("totalPages", routeService.getCountPage(user));
        req.setAttribute("page", page);
        req.getRequestDispatcher("/templates/routes.jsp").forward(req, resp);
    }
}
