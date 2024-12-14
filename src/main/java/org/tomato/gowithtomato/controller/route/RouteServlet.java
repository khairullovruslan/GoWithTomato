package org.tomato.gowithtomato.controller.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.route.NewRouteResponse;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

/*
Создание нового маршрута
 */
@Slf4j
@WebServlet("/new-route")
public class RouteServlet extends BaseServlet {
    private AjaxUtil ajaxUtil;
    private RouteService routeService;
    private ObjectMapper objectMapper;
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        routeService = ServiceFactory.getRouteService();
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        authService = ServiceFactory.getAuthService();
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/templates/route.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        NewRouteResponse routeResponse = objectMapper.readValue(req.getInputStream(), NewRouteResponse.class);
        RouteDTO routeDTO = routeResponse.routeDTO();

        log.info("Создание нового маршрута");
        if (routeResponse.type().equals("location-info")) {
            UserDTO user = authService.getUser(req);

            routeService.saveRoute(routeDTO, user.getId());
            ajaxUtil.senderRespUrl("%s/profile/routes".formatted(req.getContextPath()), resp);
            return;
        }
        ajaxUtil.senderErrorMessage("location-info не найден", resp);

    }
}

