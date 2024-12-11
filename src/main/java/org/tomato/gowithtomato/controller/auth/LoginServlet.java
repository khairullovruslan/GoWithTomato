package org.tomato.gowithtomato.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserLoginDTO;
import org.tomato.gowithtomato.exception.auth.WrongPasswordException;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

/*
 Аутентификация
 */
@Slf4j
@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private AuthService authService;

    private AjaxUtil ajaxUtil;

    private ObjectMapper objectMapper;


    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
    }

    /*
    Аутентификация пользователя
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Процесс аутентификации....");
        try {
            UserLoginDTO userLoginDTO = objectMapper.readValue(req.getInputStream(), UserLoginDTO.class);
            authService.login(userLoginDTO, req);
            ajaxUtil.senderRespUrl("%s/profile"
                    .formatted(req.getContextPath()), resp);
        } catch (WrongPasswordException ignored) {
            ajaxUtil.senderErrorMessage("Неправильный пароль", resp);

        } catch (UserNotFoundException ignored) {
            ajaxUtil.senderErrorMessage("Пользователь не найден", resp);

        }
    }
}
