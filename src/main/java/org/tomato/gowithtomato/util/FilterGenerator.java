package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.service.SessionAndCookieService;
import org.tomato.gowithtomato.service.TripService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FilterGenerator {
    private SessionAndCookieService sessionAndCookieService = SessionAndCookieService.getInstance();
    public Map<String, String> generateFilter(HttpServletRequest req) {
        Map<String, String> filter = new HashMap<>();
        processParameter(req, filter, "from");
        processParameter(req, filter, "to");
        processParameter(req, filter, "count");
        String page = req.getParameter("currentPage");
        page = page == null ? "1" : page;
        filter.put("page", page);

        String organizer = req.getParameter("organizer");
        if (organizer != null) {
            req.setAttribute("organizer", organizer);
            filter.put("organizer", organizer);
        }


        String status = req.getParameter("status");
        if (status != null) {
            req.setAttribute("status", status);
            filter.put("status", status);
        }

        boolean userTrips = Boolean.parseBoolean(req.getParameter("owner_tickets"));
        if (userTrips) {
            req.setAttribute("owner_tickets", true);
            UserDTO user;
            user = sessionAndCookieService.findUser(req);
            filter.put("owner_tickets", String.valueOf(user.getId()));
        }

        String date = req.getParameter("date");
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime localDateTime = LocalDate.parse(date, formatter).atStartOfDay();
            req.setAttribute("date", localDateTime.format(formatter));
            filter.put("date", localDateTime.toString());
        }
        req.setAttribute("page", Integer.parseInt(page));

        return filter;
    }

    private void processParameter(HttpServletRequest req, Map<String, String> filter, String paramName) {
        String paramValue = req.getParameter(paramName);
        if (paramValue != null) {
            req.setAttribute(paramName, paramValue);
            filter.put(paramName, capitalizeString(paramValue));
        }
    }

    private String capitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
