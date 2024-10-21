package org.tomato.gowithtomato.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.TemplateEngine;
import org.tomato.gowithtomato.service.*;
import org.tomato.gowithtomato.util.*;

@WebListener
public class GoWithTomatoBaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        TemplateEngine templateEngine = ThymeleafUtil.getInstance().buildTemplateEngine(servletContext);


        servletContext.setAttribute("authService", new AuthService());
        servletContext.setAttribute("userService", UserService.getInstance());
        servletContext.setAttribute("tripService", TripService.getInstance());
        servletContext.setAttribute("routeService", RouteService.getInstance());
        servletContext.setAttribute("graphHopperApiService", GraphHopperApiService.getInstance());
        servletContext.setAttribute("sessionAndCookieService", SessionAndCookieService.getInstance());
        servletContext.setAttribute("tripParticipantsService", TripParticipantsService.getInstance());


        servletContext.setAttribute("ajaxUtil", AjaxUtil.getInstance());
        servletContext.setAttribute("thymeleafUtil", ThymeleafUtil.getInstance());
        servletContext.setAttribute("filterGenerator", new FilterGenerator());
        servletContext.setAttribute("dateFormatter", new DateFormatter());
        servletContext.setAttribute("templateEngine", templateEngine);
        servletContext.setAttribute("exceptionHandler", new ExceptionHandler(templateEngine, servletContext));
        servletContext.setAttribute("objectMapper", new ObjectMapper());








    }
}
