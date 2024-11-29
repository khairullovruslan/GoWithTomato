package org.tomato.gowithtomato.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.tomato.gowithtomato.service.*;
import org.tomato.gowithtomato.util.AjaxUtil;
import org.tomato.gowithtomato.util.DateFormatter;
import org.tomato.gowithtomato.util.ExceptionHandler;
import org.tomato.gowithtomato.util.FilterGenerator;

@WebListener
public class GoWithTomatoBaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();


        servletContext.setAttribute("authService", AuthService.getInstance());
        servletContext.setAttribute("userService", UserService.getInstance());
        servletContext.setAttribute("tripService", TripService.getInstance());
        servletContext.setAttribute("routeService", RouteService.getInstance());
        servletContext.setAttribute("graphHopperApiService", GraphHopperApiService.getInstance());
        servletContext.setAttribute("tripParticipantsService", TripParticipantsService.getInstance());
        servletContext.setAttribute("reviewService", ReviewService.getInstance());


        servletContext.setAttribute("ajaxUtil", AjaxUtil.getInstance());
        servletContext.setAttribute("filterGenerator", new FilterGenerator());
        servletContext.setAttribute("dateFormatter", new DateFormatter());
        servletContext.setAttribute("exceptionHandler", new ExceptionHandler());
        servletContext.setAttribute("objectMapper", new ObjectMapper());


    }
}
