package org.tomato.gowithtomato.util;


import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.exception.ServletInitializationException;

import java.util.List;

@Slf4j
public class ExceptionHandler {
    private final TemplateEngine templateEngine;
    private final ServletContext context;
    private final ThymeleafUtil thymeleafUtil;
    public ExceptionHandler(TemplateEngine templateEngine, ServletContext servletContext) {
        this.templateEngine = templateEngine;
        this.context = servletContext;
        thymeleafUtil = ThymeleafUtil.getInstance();
    }

    @SneakyThrows
    protected void processTemplate(String templateName, HttpServletRequest request, HttpServletResponse response,
                                   List<String> errorList) {
        WebContext context = thymeleafUtil.buildWebContext(request, response, this.context);
        log.info("Processing template: {}", templateName);
        if (!errorList.isEmpty()) {
            context.setVariable("errorList", errorList);
        }
        templateEngine.process(templateName, context, response.getWriter());
    }

    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) {
        try {


        } catch (Exception notFoundException) {
            log.error("NOT FOUND EXCEPTION");
            throw new RuntimeException();
        }

    }

    @SneakyThrows
    public void handle(ServletInitializationException e) {
        log.error("ServletInitializationException caught", e);
        throw e;
    }




}
