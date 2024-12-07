package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class AjaxUtil {
    private final static AjaxUtil INSTANCE = new AjaxUtil();

    private AjaxUtil() {
    }

    public static AjaxUtil getInstance() {
        return INSTANCE;
    }

    public void senderRespUrl(String url, HttpServletResponse resp) {
        String template = "{\"url\": \"%s\"}";
        String json = template.formatted(url);
        send(resp, HttpServletResponse.SC_MOVED_PERMANENTLY, json);

    }

    public void senderErrorMessage(String errorMessage, HttpServletResponse resp) {
        String template = "{\"error\": \"%s\"}";
        String json = template.formatted(errorMessage);
        send(resp, HttpServletResponse.SC_BAD_REQUEST, json);


    }

    public void senderRouteInfo(HttpServletResponse resp, HashMap<String, String> info) {
        String template = "{\"time\": \"%s\", \"distance\": \"%s\"}";
        String json = template.formatted(info.get("time"), info.get("distance"));
        send(resp, HttpServletResponse.SC_OK, json);

    }

    public void senderRespSuccessTitle(HttpServletResponse resp) {
        String template = "{\"message\": \"%s\"}".formatted("Данные успешно обновлены");
        send(resp, HttpServletResponse.SC_CREATED, template);
    }

    private void send(HttpServletResponse resp, int code, String json) {
        try {
            resp.setContentType("application/json");
            resp.setStatus(code);
            PrintWriter out = resp.getWriter();
            out.println(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
