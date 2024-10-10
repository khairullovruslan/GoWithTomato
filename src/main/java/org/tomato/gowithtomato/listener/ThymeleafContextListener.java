package org.tomato.gowithtomato.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.TemplateEngine;
import org.tomato.gowithtomato.util.ThymeleafUtil;

@WebListener
public class ThymeleafContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ThymeleafUtil thymeleafUtil = ThymeleafUtil.getInstance();
        ServletContext servletContext = sce.getServletContext();

        TemplateEngine templateEngine = thymeleafUtil.buildTemplateEngine(servletContext);

        servletContext.setAttribute("templateEngine", templateEngine);
    }
}
