package org.tomato.gowithtomato.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.service.UserService;

import java.io.IOException;


@WebServlet("/profile")
public class UserProfileServlet extends BaseServlet {
    private UserService userService;
    @Override
    public void init() {
        super.init();
        userService = (UserService) this.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String name = req.getParameter("u");
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        if (name == null){
            String login = (String) session.getAttribute("user");
            context.setVariable("userData", userService.findUserByLogin(login));
            context.setVariable("isOwner", true);
            processTemplate(context, "profile", req, resp);
        }
        else{
            context.setVariable("userData",  userService.findUserByLogin(name));
            processTemplate(context, "profile", req, resp);
        }
    }
}
