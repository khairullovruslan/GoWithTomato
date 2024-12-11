package org.tomato.gowithtomato.controller.profile.edit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserEditDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.util.AjaxUtil;
import org.tomato.gowithtomato.validator.UserEditValidator;

import java.io.IOException;
import java.util.List;
/*
Редактирование основных данных пользователя
 */
@Slf4j
@WebServlet("/profile/edit")
public class EditProfileServlet extends BaseServlet {
    private AuthService authService;

    private UserService userService;
    private ObjectMapper objectMapper;

    private AjaxUtil ajaxUtil;

    private UserEditValidator userEditValidator;


    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        userService = ServiceFactory.getUserService();
        userEditValidator = (UserEditValidator) this.getServletContext().getAttribute("userEditValidator");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", authService.getUser(req));
        req.getRequestDispatcher("/WEB-INF/templates/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserEditDTO userEditDTO = objectMapper.readValue(req.getInputStream(), UserEditDTO.class);
        UserDTO userDTO = authService.getUser(req);
        List<String> violations = userEditValidator.isValid(userDTO, userEditDTO);

        log.info("Редактирование данных пользователя - {}", userDTO.getLogin());

        if (!violations.isEmpty()) {
            log.error("Пользователь ввел невалидные данные!");
            ajaxUtil.senderErrorMessage(String.join(", ", violations), resp);
            return;
        }
        authService.updateUser(req, userService.edit(authService.getUser(req), userEditDTO));
        ajaxUtil.senderRespSuccessTitle(resp, 203);
    }
}
