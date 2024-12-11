package org.tomato.gowithtomato.controller.route;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.RouteService;

import java.io.IOException;
import java.util.List;

/*
Все маршруты юзера
 */
@Slf4j
@WebServlet("/profile/routes")
public class RoutesServlet extends BaseServlet {
    private RouteService routeService;
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        routeService = ServiceFactory.getRouteService();
        authService = ServiceFactory.getAuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Вывод список всех маршрутов");

        String page = req.getParameter("page");
        if (page == null) page = "1";

        UserDTO user = authService.getUser(req);
        List<RouteDTO> routeDTOList = routeService.findByUserWithPagination(user, Integer.parseInt(page));

        req.setAttribute("routeList", routeDTOList);
        req.setAttribute("totalPages", routeService.getCountPage(user));
        req.setAttribute("page", page);

        req.getRequestDispatcher("/WEB-INF/templates/routes.jsp").forward(req, resp);
    }
}
