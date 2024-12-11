package org.tomato.gowithtomato.controller.profile.edit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserPasswordEditDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.util.AjaxUtil;
import org.tomato.gowithtomato.validator.auth.PasswordUserValidator;

import java.io.IOException;
import java.util.List;

/*
Редактирование пароля пользователя
 */
@Slf4j
@WebServlet("/profile/edit/password")
public class EditPasswordServlet extends BaseServlet {
    private AuthService authService;

    private ObjectMapper objectMapper;

    private AjaxUtil ajaxUtil;
    private PasswordUserValidator passwordUserValidator;

    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        passwordUserValidator = (PasswordUserValidator) this.getServletContext().getAttribute("passwordUserValidator");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserPasswordEditDTO userEditDTO = objectMapper.readValue(req.getInputStream(), UserPasswordEditDTO.class);
        UserDTO userDTO = authService.getUser(req);
        List<String> violations = passwordUserValidator.
                validate(validator, userEditDTO, userDTO);

        log.info("Изменение пароля пользователя {}", userDTO.getLogin());
        if (!violations.isEmpty()) {
            log.error("Пользователь ввел невалидные данные при изменении пароля!");
            ajaxUtil.senderErrorMessage(String.join(", ", violations), resp);
            return;
        }
        authService.updateUserPassword(userEditDTO.newPassword(), userDTO.getId());
        ajaxUtil.senderRespSuccessTitle(resp, 200);
    }
}
