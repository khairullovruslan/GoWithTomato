package org.tomato.gowithtomato.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.context.WebContext;
import org.tomato.gowithtomato.dto.RouteDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.UserService;

import java.util.List;


@WebServlet("/profile")
public class UserProfileServlet extends BaseServlet {
    private UserService userService;
    private RouteService routeService;
    @Override
    public void init() {
        super.init();
        routeService = RouteService.getInstance();
        userService = (UserService) this.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        String name = req.getParameter("u");
        WebContext context = thymeleafUtil.buildWebContext(req, resp, getServletContext());
        if (name == null){
            String login = (String) session.getAttribute("user");
            UserDTO user = userService.findUserByLogin(login);
            context.setVariable("userData", user);
            context.setVariable("isOwner", true);
            List<RouteDTO> routeDTOList = routeService.findByUser(user);
            context.setVariable("routeList", routeDTOList);
            processTemplate(context, "profile", req, resp);
        }
        else{
            context.setVariable("userData",  userService.findUserByLogin(name));
            processTemplate(context, "profile", req, resp);
        }
    }
}
