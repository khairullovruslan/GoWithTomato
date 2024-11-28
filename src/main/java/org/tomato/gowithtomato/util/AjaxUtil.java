package org.tomato.gowithtomato.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.util.HashMap;

public class AjaxUtil {
    private final static AjaxUtil INSTANCE = new AjaxUtil();
    private AjaxUtil(){}

    public static AjaxUtil getInstance() {
        return INSTANCE;
    }
    @SneakyThrows
    public void senderRespUrl(String url, HttpServletResponse resp){
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.print("{\"url\": \"" + url + "\"}");
        out.flush();
    }
    @SneakyThrows
    public void senderRouteInfo(HttpServletResponse resp, HashMap<String, String> info){
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.print("{\"time\": \"" + info.get("time") + "\", \"distance\": \"" + info.get("distance") +  "\"}");
        out.flush();
    }

    public static void main(String[] args) {
        String s = "{\"url\": \"" + "asdasdasd" + "\", \"url\": \"" + "123" + "}";
        System.out.println(s);
    }
}
