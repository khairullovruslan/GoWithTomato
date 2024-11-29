package org.tomato.gowithtomato.controller.trip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

@WebServlet("/create-trip")
public class TripCreateServlet extends BaseServlet {
    private AuthService authService;
    private AjaxUtil ajaxUtil;
    private TripService tripService;
    private RouteService routeService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        super.init();
        authService = (AuthService) this.getServletContext().getAttribute("authService");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        tripService = (TripService) this.getServletContext().getAttribute("tripService");
        routeService = (RouteService) this.getServletContext().getAttribute("routeService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        RouteDTO routeDTO = routeService.findById(id);
        req.setAttribute("routeId", routeDTO.getId());
        req.getRequestDispatcher("/templates/create-trip.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        TripDTO tripDTO = objectMapper.readValue(req.getInputStream(), TripDTO.class);
        Long id = Long.valueOf(req.getParameter("id"));
        tripService.saveTrip(authService.getUser(req), tripDTO, id);

        ajaxUtil.senderRespUrl(req.getContextPath() + "/profile", resp);


    }
}
