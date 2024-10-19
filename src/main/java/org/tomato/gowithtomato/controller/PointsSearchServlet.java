package org.tomato.gowithtomato.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.service.GraphHopperApiService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@WebServlet("/points-search")
public class PointsSearchServlet extends BaseServlet{
    private GraphHopperApiService graphHopperApiService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        super.init();
        graphHopperApiService = (GraphHopperApiService) this.getServletContext().getAttribute("graphHopperApiService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");

    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        String query = req.getParameter("q");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        if (query != null){
            List<PointDTO> points = graphHopperApiService.getPointsByName(query);
            log.info("Количество найденных точек по названию {} - {}", query, points.size());
            out.print(objectMapper.writeValueAsString(points));
        }
        else {
            log.error("Параметр query пуст!");
            out.print(objectMapper.writeValueAsString(new ArrayList<>()));
        }
        out.flush();
    }
}
