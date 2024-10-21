package org.tomato.gowithtomato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
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
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        super.init();
        sessionAndCookieService = (SessionAndCookieService) this.getServletContext().getAttribute("sessionAndCookieService");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        tripService = (TripService) this.getServletContext().getAttribute("tripService");
        routeService = (RouteService) this.getServletContext().getAttribute("routeService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        Long id = Long.valueOf(req.getParameter("id"));
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        RouteDTO routeDTO = routeService.findById(id);
        context.setVariable("route", routeDTO);
        processTemplate(context,"create-trip",  req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        TripDTO tripDTO = objectMapper.readValue(req.getInputStream(), TripDTO.class);
        Long id = Long.valueOf(req.getParameter("id"));
        try {
            tripService.saveTrip(sessionAndCookieService.findUser(req), tripDTO, id);

            ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);
        }
        catch (UserNotFoundException e){
            ajaxUtil.senderRespUrl(req.getContextPath() + "/login", resp);
        }
    }
}
