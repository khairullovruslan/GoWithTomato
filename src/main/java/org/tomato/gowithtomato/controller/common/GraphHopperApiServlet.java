package org.tomato.gowithtomato.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.dto.NewRouteResponse;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.service.GraphHopperApiService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

@WebServlet("/graph-hopper-api")
public class GraphHopperApiServlet extends BaseServlet {
    private AjaxUtil ajaxUtil;
    private ObjectMapper objectMapper;
    private GraphHopperApiService graphHopperApiService;

    @Override
    public void init() {
        super.init();
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        graphHopperApiService = (GraphHopperApiService) this.getServletContext().getAttribute("graphHopperApiService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        NewRouteResponse routeResponse = objectMapper.readValue(req.getInputStream(), NewRouteResponse.class);
        RouteDTO routeDTO = routeResponse.getRouteDTO();

        try {
            ajaxUtil.senderRouteInfo(resp, graphHopperApiService.getInfo(routeDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
