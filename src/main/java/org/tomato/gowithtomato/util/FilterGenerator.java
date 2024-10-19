package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletRequest;
import org.thymeleaf.context.WebContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FilterGenerator {
    public Map<String, String> generateFilter(WebContext context, HttpServletRequest req) {
        Map<String, String> filter = new HashMap<>();
        processParameter(context, req, filter, "from");
        processParameter(context, req, filter, "to");
        processParameter(context, req, filter, "count");

        String date = req.getParameter("date");
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime localDateTime = LocalDate.parse(date, formatter).atStartOfDay();
            context.setVariable("date", localDateTime.format(formatter));
            filter.put("date", localDateTime.toString());
        }

        return filter;
    }

    private void processParameter(WebContext context, HttpServletRequest req, Map<String, String> filter, String paramName) {
        String paramValue = req.getParameter(paramName);
        if (paramValue != null) {
            context.setVariable(paramName, paramValue);
            filter.put(paramName, capitalizeString(paramValue));
        }
    }

    private String capitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
