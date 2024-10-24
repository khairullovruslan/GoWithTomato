package org.tomato.gowithtomato.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.DateFormatter;
import org.tomato.gowithtomato.util.FilterGenerator;

import java.io.IOException;
import java.util.HashMap;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var filter = filterGenerator.generateFilter(req);
        List<TripDTO> tripList = tripService.findByFilter(filter);
        tripList.forEach(trip -> {
            String formattedDateTime = dateFormatter.format(trip.getTripDateTime());
            trip.setTripDateTimeFormatted(formattedDateTime);
        });
        req.setAttribute("tripList", tripList);
        req.setAttribute("totalPages",  tripService.getCountPage(filter));
        getServletContext().getRequestDispatcher("/templates/trips.jsp").forward(req, resp);
    }



}
