package org.tomato.gowithtomato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.UserNotFoundException;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.SessionAndCookieService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

@WebServlet("/create-trip")
public class TripCreateServlet extends BaseServlet{
    private SessionAndCookieService sessionAndCookieService;
    private AjaxUtil ajaxUtil;
    private TripService tripService;
    private RouteService routeService;

    @Override
    public void init() {
        super.init();
        sessionAndCookieService = SessionAndCookieService.getInstance();
        ajaxUtil = AjaxUtil.getInstance();
        tripService = TripService.getInstance();
        routeService = RouteService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        RouteDTO routeDTO = routeService.findById(id).get();
        // todo добавить проверку
        context.setVariable("route", routeDTO);
        processTemplate(context,"create-trip",  req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TripDTO tripDTO = objectMapper.readValue(req.getInputStream(), TripDTO.class);
        System.out.println(tripDTO);
        Long id = Long.valueOf(req.getParameter("id"));
        UserDTO user;
        try {
            user = sessionAndCookieService.findUser(req);
            tripDTO.setOwner(user);
            tripDTO.setRoute(routeService.findById(id).get());
            tripService.saveTrip(tripDTO, id);

            ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);
        }
        catch (UserNotFoundException e){
            ajaxUtil.senderRespUrl(req.getContextPath() + "/login", resp);
        }
    }
}
