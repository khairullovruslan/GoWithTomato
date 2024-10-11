package org.tomato.gowithtomato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.mapper.RouteMapper;
import org.tomato.gowithtomato.dao.RouteDAOImpl;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

@WebServlet("/new-route")
public class RouteServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processTemplate("route", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AjaxUtil ajaxUtil =  AjaxUtil.getInstance();
        RouteDTO routeDTO = objectMapper.readValue(req.getInputStream(), RouteDTO.class);
        RouteDAOImpl routeDAO = RouteDAOImpl.getInstance();
        routeDAO.save(RouteMapper.getInstance().convertDTOToRoute(routeDTO));
        ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);
    }
}
