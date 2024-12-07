package org.tomato.gowithtomato.controller.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        req.getRequestDispatcher("/templates/route.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewRouteResponse routeResponse = objectMapper.readValue(req.getInputStream(), NewRouteResponse.class);
        RouteDTO routeDTO = routeResponse.getRouteDTO();
        if (routeResponse.getType().equals("location-info")) {
            UserDTO user = authService.getUser(req);
            routeDTO.setOwner(user);
            routeService.saveRoute(RouteMapper.getInstance().convertDTOToRoute(routeDTO));
            ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);
        }
    }
}

