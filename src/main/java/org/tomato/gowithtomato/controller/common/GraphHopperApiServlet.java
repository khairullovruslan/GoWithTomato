package org.tomato.gowithtomato.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.dto.route.NewRouteResponse;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.exception.common.GraphHopperApiException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.GraphHopperApiService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

/*
 Сервлет для GraphHopperApi
 */
@Slf4j
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
        graphHopperApiService = ServiceFactory.getGraphHopperApiService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        NewRouteResponse routeResponse = objectMapper.readValue(req.getInputStream(), NewRouteResponse.class);
        RouteDTO routeDTO = routeResponse.routeDTO();

        try {
            log.info("Получение информации маршрута");

            resp.setStatus(200);

            ajaxUtil.senderRouteInfo(resp, graphHopperApiService.getInfo(routeDTO));
        } catch (Exception e) {
            throw new GraphHopperApiException("Ошибка при получение информации маршрута", e);
        }


    }
}
