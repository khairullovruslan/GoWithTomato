package org.tomato.gowithtomato.controller.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/*
 Регистация
 */
@Slf4j
@WebServlet("/sign-up")
public class SignUpServlet extends BaseServlet {
    private AuthService authService;
    private ObjectMapper objectMapper;
    private AjaxUtil ajaxUtil;


    @Override
    public void init() {
        super.init();
        authService = ServiceFactory.getAuthService();
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/templates/registration.jsp").forward(req, resp);
    }

    /*
     Регистрации пользователя
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Процесс регистрации пользователя");
        /*
         Проверка на валидность
         */
        UserRegistrationDTO userRegistrationDto = objectMapper.readValue(req.getInputStream(), UserRegistrationDTO.class);
        System.out.println(userRegistrationDto);
        Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(userRegistrationDto);

        if (!violations.isEmpty()) {
            log.error("Пользователь ввел невалидные данные!");
            ajaxUtil.senderErrorMessage(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";")), resp);
            return;
        }
        authService.registration(userRegistrationDto);
        ajaxUtil.senderRespUrl("%s/login"
                .formatted(req.getContextPath()), resp);


    }
}
