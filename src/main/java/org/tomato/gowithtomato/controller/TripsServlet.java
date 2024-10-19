package org.tomato.gowithtomato.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.DateFormatter;
import org.tomato.gowithtomato.util.FilterGenerator;

import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/trips")
public class TripsServlet extends BaseServlet{
    private TripService tripService;
    private FilterGenerator filterGenerator;
    private DateFormatter dateFormatter;
    @Override
    public void init() {
        super.init();
        tripService = (TripService) this.getServletContext().getAttribute("tripService");
        filterGenerator = (FilterGenerator) this.getServletContext().getAttribute("filterGenerator");
        dateFormatter = (DateFormatter) this.getServletContext().getAttribute("dateFormatter");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        List<TripDTO> tripList = tripService.findByFilter(filterGenerator.generateFilter(context, req));
        tripList.forEach(trip -> {
            String formattedDateTime = dateFormatter.format(trip.getTripDateTime());
            trip.setTripDateTimeFormatted(formattedDateTime);
        });

        context.setVariable("tripList", tripList);
        processTemplate(context, "trips", req, resp);
    }



}
