package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class FilterGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DEFAULT_PAGE = "1";
    private final AuthService authService;

    public FilterGenerator() {
        this.authService = ServiceFactory.getAuthService();
    }


    public Map<String, String> generateFilter(final HttpServletRequest req) {
        Map<String, String> filter = new HashMap<>();

        extractAndProcessParameter(req, filter, "from");
        extractAndProcessParameter(req, filter, "to");
        extractAndProcessParameter(req, filter, "count");

        String pageParam = req.getParameter("page");
//        log.error("page param "  + pageParam);
        int page = Integer.parseInt(pageParam != null ? pageParam : DEFAULT_PAGE);
        filter.put("page", String.valueOf(page));
        req.setAttribute("page", page);

        extractAndProcessParameter(req, filter, "organizer");
        extractAndProcessParameter(req, filter, "status");

        processOwnerTicketsParameter(req, filter);
        processDateParameter(req, filter);

        log.error("filter - " + filter);
        return filter;
    }

    private void processOwnerTicketsParameter(final HttpServletRequest req, final Map<String, String> filter) {
        String ownerTicketsParam = req.getParameter("owner_tickets");
        if (ownerTicketsParam != null && Boolean.parseBoolean(ownerTicketsParam)) {
            req.setAttribute("owner_tickets", true);
            try {
                filter.put("owner_tickets", String.valueOf(authService.getUser(req).getId()));
            } catch (UnauthorizedException e) {
                throw new UnauthorizedException("Доступ запрещен");
            }
        }
    }

    private void processDateParameter(HttpServletRequest req, Map<String, String> filter) {
        String dateParam = req.getParameter("date");
        if (dateParam != null) {
            try {
                LocalDate date = LocalDate.parse(dateParam, DATE_FORMATTER);
                LocalDateTime localDateTime = date.atStartOfDay();
                req.setAttribute("date", date.format(DATE_FORMATTER));
                filter.put("date", localDateTime.toString());
            } catch (Exception e) {
            }
        }
    }


    private void extractAndProcessParameter(HttpServletRequest req, Map<String, String> filter, String paramName) {
        String paramValue = req.getParameter(paramName);
        if (paramValue != null) {
            req.setAttribute(paramName, paramValue);
            filter.put(paramName, paramValue);
        }
    }

}