package org.tomato.gowithtomato.controller.trip;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.DateFormatter;
import org.tomato.gowithtomato.util.FilterGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/*
Страница со всеми поездками
 */
@Slf4j
@WebServlet("/trips")
public class TripsServlet extends BaseServlet {
    private TripService tripService;
    private FilterGenerator filterGenerator;
    private DateFormatter dateFormatter;

    @Override
    public void init() {
        super.init();
        tripService = ServiceFactory.getTripService();
        filterGenerator = (FilterGenerator) this.getServletContext().getAttribute("filterGenerator");
        dateFormatter = (DateFormatter) this.getServletContext().getAttribute("dateFormatter");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Вывод всех поездок по фильтру");

        Map<String, String> filter = filterGenerator.generateFilter(req);
        List<TripDTO> tripList = tripService.findByFilter(filter);
        log.debug("Размер списка поездок - {}", tripList.size());
        /*
        Подстановка форматированного времени
         */
        tripList.forEach(trip -> {
            String formattedDateTime = dateFormatter.format(trip.getTripDateTime());
            trip.setTripDateTimeFormatted(formattedDateTime);
        });

        req.setAttribute("tripList", tripList);
        req.setAttribute("totalPages", tripService.getCountPage(filter));

        getServletContext().getRequestDispatcher("/WEB-INF/templates/trips.jsp").forward(req, resp);
    }


}
