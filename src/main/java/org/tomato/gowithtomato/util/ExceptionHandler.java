package org.tomato.gowithtomato.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.exception.auth.RegistrationException;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.exception.common.IncorrectRequestParametersException;
import org.tomato.gowithtomato.exception.common.ServletInitializationException;
import org.tomato.gowithtomato.exception.db.RoutNotFoundException;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExceptionHandler {


    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) {
        try {
            switch (e) {
                case RegistrationException exc -> {
                    req.setAttribute("errorList",
                            exc.getViolations()
                                    .stream()
                                    .map(ConstraintViolation::getMessage)
                                    .toList());
                    req.getRequestDispatcher("/WEB-INF/templates/registration.jsp").forward(req, resp);
                }

                case UserNotFoundException ignored -> {
                    req.setAttribute("errorList", new ArrayList<>(List.of("Пользователь с таким логином не был найден")));
                    req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
                }
                case RoutNotFoundException ignored ->
                        redirectToErrorPage(req, resp, "Маршрут не найден! Попробуйте еще раз.");

                case IncorrectRequestParametersException ignored ->
                        redirectToErrorPage(req, resp, "Некорректные параметры запроса! Попробуйте еще раз.");

                case UnauthorizedException ignored ->
                        req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
                default -> throw new Exception();
            }

        } catch (Exception notFoundException) {
            log.error("Ошибка при обработке {} запроса на {}: {}", req.getMethod(), req.getServletPath(), notFoundException.getMessage(), notFoundException);
            redirectToErrorPage(req, resp, "Не удалось выполнить ваше действие. Пожалуйста, попробуйте позже.");
        }

    }

    @SneakyThrows
    public void handle(ServletInitializationException e) {
        log.error("ServletInitializationException caught", e);
        throw e;
    }

    @SneakyThrows
    private void redirectToErrorPage(HttpServletRequest req, HttpServletResponse resp, String message) {
        req.setAttribute("errorMessage", message);
        resp.sendRedirect("%s/error".formatted(req.getContextPath()));
    }


}
