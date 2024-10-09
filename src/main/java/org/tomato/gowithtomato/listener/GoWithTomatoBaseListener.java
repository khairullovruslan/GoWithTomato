package org.tomato.gowithtomato.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDaoImpl;
import org.tomato.gowithtomato.service.AuthService;

@WebListener
public class GoWithTomatoBaseListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        UserDaoImpl userDao = new UserDaoImpl();
        ModelMapper modelMapper = new ModelMapper();



        AuthService authService = new AuthService(userDao, modelMapper);


        servletContext.setAttribute("authService", authService);

    }
}
