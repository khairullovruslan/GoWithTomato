package org.tomato.gowithtomato.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.UserService;


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
        UserDTO whoChecked;
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        if (name == null){
            String login = (String) session.getAttribute("user");
             whoChecked = userService.findUserByLogin(login);
        }
        else{
            UserDTO userDTO = userService.findUserByLogin(name);
            context.setVariable("userData", userDTO);
            processTemplate(context, "profile", req, resp);
        }
    }
}
