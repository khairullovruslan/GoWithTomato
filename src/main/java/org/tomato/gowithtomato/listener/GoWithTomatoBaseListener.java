package org.tomato.gowithtomato.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.modelmapper.ModelMapper;
import org.thymeleaf.TemplateEngine;
import org.tomato.gowithtomato.service.*;
import org.tomato.gowithtomato.util.*;

@WebListener
public class GoWithTomatoBaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();


        servletContext.setAttribute("authService", new AuthService());
        servletContext.setAttribute("userService", UserService.getInstance());
        TemplateEngine templateEngine = ThymeleafUtil.getInstance().buildTemplateEngine(servletContext);
        servletContext.setAttribute("templateEngine", templateEngine);
        servletContext.setAttribute("thymeleafUtil", ThymeleafUtil.getInstance());
        servletContext.setAttribute("exceptionHandler", new ExceptionHandler(templateEngine, servletContext));;
        servletContext.setAttribute("graphHopperApiService", GraphHopperApiService.getInstance());
        servletContext.setAttribute("objectMapper", new ObjectMapper());

        servletContext.setAttribute("ajaxUtil", AjaxUtil.getInstance());
        servletContext.setAttribute("routeService", RouteService.getInstance());
        servletContext.setAttribute("sessionAndCookieService", SessionAndCookieService.getInstance());
        servletContext.setAttribute("tripService", TripService.getInstance());
        servletContext.setAttribute("tripParticipantsService", TripParticipantsService.getInstance());
        servletContext.setAttribute("filterGenerator", new FilterGenerator());
        servletContext.setAttribute("dateFormatter", new DateFormatter());



    }
}
