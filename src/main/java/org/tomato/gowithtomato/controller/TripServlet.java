package org.tomato.gowithtomato.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.IncorrectRequestParametersException;
import org.tomato.gowithtomato.service.SessionAndCookieService;
import org.tomato.gowithtomato.service.TripParticipantsService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.DateFormatter;

import java.io.IOException;

@Slf4j
@WebServlet("/trip/*")
public class TripServlet extends BaseServlet {
    private TripService tripService;
    private TripParticipantsService tripParticipantsService;
    private SessionAndCookieService sessionAndCookieService;
    private DateFormatter dateFormatter;

    @Override
    public void init() {
        super.init();
        sessionAndCookieService = (SessionAndCookieService) this.getServletContext().getAttribute("sessionAndCookieService");
        tripService = (TripService) this.getServletContext().getAttribute("tripService");
        tripParticipantsService = (TripParticipantsService) this.getServletContext().getAttribute("tripParticipantsService");
        dateFormatter = (DateFormatter) this.getServletContext().getAttribute("dateFormatter");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            try {
                String tripIdString = pathInfo.substring(1);
                long tripId = Long.parseLong(tripIdString);
                WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
                TripDTO tripDTO = setFormattedDateTime(tripService.findById(tripId));

                context.setVariable("tripInfo", generateTripInfo(tripDTO));
                context.setVariable("trip", tripDTO);
                context.setVariable("members", tripParticipantsService.findUsersByTripId(tripId));

                processTemplate(context, "trip", req, resp);
            } catch (NumberFormatException e) {
                log.error("Неверный формат идентификатора поездки: {}", pathInfo, e);
                throw new IncorrectRequestParametersException();
            }
        } else {
            throw new IncorrectRequestParametersException();
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && !pathInfo.isEmpty()) {
            String routeIdString = pathInfo.substring(1);
            long tripId = Long.parseLong(routeIdString);
            UserDTO user = sessionAndCookieService.findUser(req);
            tripParticipantsService.save(tripId, user.getId());
        }
        else {
            throw new IncorrectRequestParametersException();
        }
    }

    private TripDTO setFormattedDateTime(TripDTO tripDTO) {
        String formattedDate = dateFormatter.formatTripDateTime(tripDTO.getTripDateTime());
        tripDTO.setTripDateTimeFormatted(formattedDate);
        return tripDTO;
    }

    private String generateTripInfo(TripDTO tripDTO) {
        String fromCity = tripDTO.getRoute().getStart().getName();
        String toCity = tripDTO.getRoute().getFinish().getName();
        return dateFormatter.formatTripInfo(tripDTO.getTripDateTime(), fromCity, toCity);
    }

}