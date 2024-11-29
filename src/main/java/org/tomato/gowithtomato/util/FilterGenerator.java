package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.service.AuthService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class FilterGenerator {


    public Map<String, String> generateFilter(final HttpServletRequest req) {
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
            AuthService authService = AuthService.getInstance();
            if (authService.authorizationCheck(req)) {
                filter.put("owner_tickets", String.valueOf(authService.getUser(req).getId()));
            } else {
                throw new UnauthorizedException("Нет доступа к странице");
            }
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

    private void processParameter(final HttpServletRequest req,
                                  final Map<String, String> filter, final String paramName) {
        String paramValue = req.getParameter(paramName);
        if (paramValue != null) {
            req.setAttribute(paramName, paramValue);
            filter.put(paramName, capitalizeString(paramValue));
        }
    }

    private String capitalizeString(final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
