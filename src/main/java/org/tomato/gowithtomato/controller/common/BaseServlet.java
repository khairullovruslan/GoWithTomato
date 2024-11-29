package org.tomato.gowithtomato.controller.common;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.exception.common.ServletInitializationException;
import org.tomato.gowithtomato.util.ExceptionHandler;


@Slf4j
public class BaseServlet extends HttpServlet {
    private ExceptionHandler exceptionHandler;
    protected Validator validator;

    @Override
    public void init() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
            exceptionHandler = (ExceptionHandler) this.getServletContext().getAttribute("exceptionHandler");
        } catch (Exception e) {
            exceptionHandler.handle(new ServletInitializationException());
        }
    }


    @Override
    @SneakyThrows
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        log.info("Обработка {} запроса для {}", req.getMethod(), req.getServletPath());
        try {
            super.service(req, resp);
            log.info("Успешно обработан {} запрос на {}", req.getMethod(), req.getServletPath());
        } catch (Exception e) {
            log.error("Ошибка при обработке {} запроса на {}: {}", req.getMethod(), req.getServletPath(), e.getMessage(), e);
            exceptionHandler.handle(e, req, resp);
        }
    }

}
