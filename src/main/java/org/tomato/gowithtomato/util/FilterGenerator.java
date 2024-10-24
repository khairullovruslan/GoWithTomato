package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FilterGenerator {
    public Map<String, String> generateFilter(HttpServletRequest req) {
        Map<String, String> filter = new HashMap<>();
        processParameter(req, filter, "from");
        processParameter(req, filter, "to");
        processParameter(req, filter, "count");
        String page = req.getParameter("currentPage");
        page = page == null ? "1" : page;
        filter.put("page", page);

        String date = req.getParameter("date");
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime localDateTime = LocalDate.parse(date, formatter).atStartOfDay();
            req.setAttribute("date", localDateTime.format(formatter));
            filter.put("date", localDateTime.toString());
        }
        System.out.println(page);
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
