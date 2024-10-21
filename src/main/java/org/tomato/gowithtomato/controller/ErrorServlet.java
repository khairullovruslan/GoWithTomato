package org.tomato.gowithtomato.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet("/error")
public class ErrorServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        context.setVariable("errorMessage", req.getParameter("errorMessage"));
        processTemplate(context,"error", req, resp);
    }
}
