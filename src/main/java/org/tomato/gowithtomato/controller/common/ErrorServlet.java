package org.tomato.gowithtomato.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/*
 Страница ошибки
 */
@Slf4j
@WebServlet("/error")
public class ErrorServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Произошла ошибка");
        log.info("Открываем страницу для ошибок");
        req.getRequestDispatcher("/WEB-INF/templates/error.jsp").forward(req, resp);
    }
}
