package org.tomato.gowithtomato.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.exception.ServletInitializationException;
import org.tomato.gowithtomato.util.ExceptionHandler;
import org.tomato.gowithtomato.util.ThymeleafUtil;


@Slf4j
public class BaseServlet extends HttpServlet {
    protected TemplateEngine templateEngine;
    private ExceptionHandler exceptionHandler;
    private ThymeleafUtil thymeleafUtil;
    protected Validator validator;

    @Override
    public void init() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            thymeleafUtil = ThymeleafUtil.getInstance();
            validator = validatorFactory.getValidator();
            templateEngine = (TemplateEngine) this.getServletContext().getAttribute("templateEngine");
            exceptionHandler = new ExceptionHandler(templateEngine, this.getServletContext());
        } catch (Exception e) {
            exceptionHandler.handle(new ServletInitializationException());
        }
    }

    @SneakyThrows
    protected void processTemplate(String templateName, HttpServletRequest request, HttpServletResponse response) {
        WebContext context = thymeleafUtil.buildWebContext(request, response, getServletContext());
        context.setVariable("context", request.getContextPath());
        log.info("Шаблон обработки: {}", templateName);
        templateEngine.process(templateName, context, response.getWriter());
    }

    @SneakyThrows
    protected void processTemplate(WebContext context, String templateName, HttpServletRequest request, HttpServletResponse response) {
        context.setVariable("context", request.getContextPath());
        templateEngine.process(templateName, context, response.getWriter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        log.info("Обработка {} запроса для {}", req.getMethod(), req.getServletPath());
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            super.service(req, resp);
            log.info("Успешно обработан {} запрос на {}", req.getMethod(), req.getServletPath());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionHandler.handle(e, req, resp);
        }
    }

}
