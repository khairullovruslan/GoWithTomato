package org.tomato.gowithtomato.controller.trip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/*
Создание поездки
 */
@Slf4j
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
        authService = ServiceFactory.getAuthService();
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        tripService = ServiceFactory.getTripService();
        routeService = ServiceFactory.getRouteService();
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RouteDTO routeDTO = routeService.findById(Long.valueOf(req.getParameter("id")));

        req.setAttribute("routeId", routeDTO.getId());
        req.getRequestDispatcher("/WEB-INF/templates/create-trip.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            TripDTO tripDTO = objectMapper.readValue(req.getInputStream(), TripDTO.class);
            Set<ConstraintViolation<TripDTO>> violations = validator.validate(tripDTO);
            if (!violations.isEmpty()) {
                log.error("Пользователь ввел невалидные данные!");
                ajaxUtil.senderErrorMessage(violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(";")), resp);
                return;
            }


            Long routeId = Long.valueOf(req.getParameter("id"));
            log.info("Сохранение поездки по маршруту по id = {}", routeId);
            Long tripId = tripService.saveTrip(authService.getUser(req), tripDTO, routeId);

            ajaxUtil.senderRespUrl("%s/trip/%s"
                    .formatted(req.getContextPath(), tripId), resp);
        } catch (InvalidFormatException e) {
            ajaxUtil.senderErrorMessage("Некорректный формат даты", resp);
        }


    }
}
