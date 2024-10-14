package org.tomato.gowithtomato.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.modelmapper.ModelMapper;
import org.thymeleaf.TemplateEngine;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.util.ThymeleafUtil;

@WebListener
public class GoWithTomatoBaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        AuthService authService = new AuthService();
        UserService userService = UserService.getInstance();


        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("userService", userService);
        TemplateEngine templateEngine = ThymeleafUtil.getInstance().buildTemplateEngine(servletContext);

        servletContext.setAttribute("templateEngine", templateEngine);

    }
}
