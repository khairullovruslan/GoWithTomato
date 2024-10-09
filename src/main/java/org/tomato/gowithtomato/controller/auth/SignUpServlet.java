package org.tomato.gowithtomato.controller.auth;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.BaseServlet;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.AuthService;

@WebServlet("/sign-up")
public class SignUpServlet extends BaseServlet {
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        authService = (AuthService) this.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        processTemplate("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        UserDTO userDTO = UserDTO
                .builder()
                .password(pwd)
                .login(login)
                .phoneNumber(phone)
                .email(email)
                .build();
        authService.registration(userDTO);
    }
}
