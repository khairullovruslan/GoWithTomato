package org.tomato.gowithtomato.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.service.GraphHopperApiService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/points-search")
public class PointsSearchServlet extends BaseServlet{
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        System.out.println(query);
        resp.setContentType("application/json");
        GraphHopperApiService graphHopperApiService = GraphHopperApiService.getInstance();

        List<PointDTO> points = graphHopperApiService.getPointsByName(query);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        out.print(objectMapper.writeValueAsString(points));
        out.flush();
    }
}
