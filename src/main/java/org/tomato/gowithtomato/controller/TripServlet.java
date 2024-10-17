package org.tomato.gowithtomato.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.service.TripParticipantsService;
import org.tomato.gowithtomato.service.TripService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@WebServlet("/trip/*")
public class TripServlet extends BaseServlet {
    private TripService tripService;
    private TripParticipantsService tripParticipantsService;
    @Override
    public void init() {
        super.init();
        tripService = TripService.getInstance();
        tripParticipantsService = TripParticipantsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {




        String pathInfo = req.getPathInfo();

        if (pathInfo != null && !pathInfo.isEmpty()) {
            String tripIdString = pathInfo.substring(1);
            try {
                WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
                long tripId = Long.parseLong(tripIdString);
                TripDTO tripDTO = tripService.findById(tripId);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM");
                String formattedDate = tripDTO.getTripDateTime().format(formatter);

                tripDTO.setTripDateTimeFormatted(formattedDate);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                String time = tripDTO.getTripDateTime().format(timeFormatter);

                String fromCity = tripDTO.getRoute().getStart().getName();
                String toCity = tripDTO.getRoute().getFinish().getName();

                String tripInfo = time + " | " + fromCity + " → " + toCity;
                context.setVariable("tripInfo", tripInfo);
                context.setVariable("trip", tripDTO);
                context.setVariable("members", tripParticipantsService.findUsersByTripId(tripId));
                processTemplate(context, "trip", req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid trip ID.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trip ID is required.");
        }
    }

}