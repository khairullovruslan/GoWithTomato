package org.tomato.gowithtomato.util;


import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.exception.RegistrationException;
import org.tomato.gowithtomato.exception.ServletInitializationException;
import org.tomato.gowithtomato.exception.UserNotFoundException;
import org.tomato.gowithtomato.exception.WrongPasswordException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExceptionHandler {
    private final TemplateEngine templateEngine;
    private final ServletContext context;
    public ExceptionHandler(TemplateEngine templateEngine, ServletContext servletContext) {
        this.templateEngine = templateEngine;
        this.context = servletContext;
    }

    @SneakyThrows
    protected void processTemplate(String templateName, HttpServletRequest request, HttpServletResponse response,
                                   List<String> errorList) {
        ThymeleafUtil thymeleafUtil = ThymeleafUtil.getInstance();
        WebContext context = thymeleafUtil.buildWebContext(request, response, this.context);
        log.info("Processing template: {}", templateName);
        if (!errorList.isEmpty()) {
            context.setVariable("errorList", errorList);
        }
        templateEngine.process(templateName, context, response.getWriter());
    }

    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) {
        try {
            switch (e) {
                case RegistrationException exc -> {
                    List<String> errors = exc.getViolations().stream()
                            .map(ConstraintViolation::getMessage)
                            .toList();
                    processTemplate("registration", req, resp, errors);
                }
                case UserNotFoundException ignored -> {
                    processTemplate("login", req, resp, new ArrayList<>(List.of("Пользователь с таким логином не был найден")));
                }
                case WrongPasswordException ignored -> {
                    processTemplate("login", req, resp, new ArrayList<>(List.of("Неверный пароль")));
                }

                default -> throw new IllegalStateException("Unexpected value: " + e);
            }

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
