package org.tomato.gowithtomato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.UserNotFoundException;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.SessionAndCookieService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

@WebServlet("/new-route")
public class RouteServlet extends BaseServlet {
    private  AjaxUtil ajaxUtil;
    private RouteService routeService;
    private SessionAndCookieService sessionAndCookieService;
    @Override
    public void init() {
        super.init();
        routeService = RouteService.getInstance();
        ajaxUtil =  AjaxUtil.getInstance();
        sessionAndCookieService = SessionAndCookieService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processTemplate("route", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RouteDTO routeDTO = objectMapper.readValue(req.getInputStream(), RouteDTO.class);
        UserDTO user;
        try {
            user = sessionAndCookieService.findUser(req);
            routeDTO.setOwner(user);
            routeService.saveRoute(RouteMapper.getInstance().convertDTOToRoute(routeDTO));
            ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);
        }
        catch (UserNotFoundException e){
            ajaxUtil.senderRespUrl(req.getContextPath() + "/login", resp);
        }
    }
}
