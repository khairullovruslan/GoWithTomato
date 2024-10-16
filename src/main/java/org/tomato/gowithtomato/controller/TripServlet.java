package org.tomato.gowithtomato.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.service.TripService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/trips")
public class TripServlet extends BaseServlet{
    private TripService tripService;
    @Override
    public void init() {
        super.init();
        tripService = TripService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        Map<String, String> filter = new HashMap<>();
        String from = req.getParameter("from");
        if (from != null){
            context.setVariable("from", from);
            filter.put("from",   from.substring(0, 1).toUpperCase() + from.substring(1).toLowerCase());
        }
        String to = req.getParameter("to");
        if (to != null){
            context.setVariable("to", to);
            filter.put("to",  to.substring(0, 1).toUpperCase() + to.substring(1).toLowerCase());
        }
        String count = req.getParameter("count");
        if (count != null){
            context.setVariable("count", count);
            filter.put("count", count);
        }
        String date = req.getParameter("date");
        if (date != null){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            context.setVariable("date",  localDateTime.format(formatter));
            filter.put("date", localDateTime.toString());
        }
        List<TripDTO> tripList = tripService.findByFilter(filter);




        tripList.forEach(trip -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDateTime = trip.getTripDateTime().format(formatter);
            trip.setTripDateTimeFormatted(formattedDateTime);
        });

        context.setVariable("tripList", tripList);
        processTemplate(context, "trip", req, resp);

    }
}
