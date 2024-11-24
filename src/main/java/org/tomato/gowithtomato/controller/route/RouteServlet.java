package org.tomato.gowithtomato.controller.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.NewRouteResponse;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.service.CookieService;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

@WebServlet("/new-route")
public class RouteServlet extends BaseServlet {
    private AjaxUtil ajaxUtil;
    private RouteService routeService;
    private CookieService cookieService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        super.init();
        routeService = (RouteService) this.getServletContext().getAttribute("routeService");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        cookieService = (CookieService) this.getServletContext().getAttribute("cookieService");
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
            UserDTO user;
            try {
                user = cookieService.findUser(req);
                routeDTO.setOwner(user);
                routeService.saveRoute(RouteMapper.getInstance().convertDTOToRoute(routeDTO));
                ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);
            } catch (UserNotFoundException e) {
                ajaxUtil.senderRespUrl(req.getContextPath() + "/login", resp);
            }
        }
    }
}

