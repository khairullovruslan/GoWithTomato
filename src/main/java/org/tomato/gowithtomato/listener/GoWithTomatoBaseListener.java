package org.tomato.gowithtomato.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.UserService;

@WebListener
public class GoWithTomatoBaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        AuthService authService = new AuthService();
        UserService userService = UserService.getInstance();


        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("userService", userService);

    }
}
