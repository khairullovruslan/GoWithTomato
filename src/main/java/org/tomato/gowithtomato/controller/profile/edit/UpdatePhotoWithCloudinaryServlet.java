package org.tomato.gowithtomato.controller.profile.edit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.CloudinaryService;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;

/*
Редактирование фотографии пользователя
 */
@Slf4j
@WebServlet("/profile/upload")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class UpdatePhotoWithCloudinaryServlet extends BaseServlet {
    private CloudinaryService cloudinaryService;
    private AuthService authService;

    private UserService userService;

    private AjaxUtil ajaxUtil;

    @Override
    public void init() {
        super.init();
        cloudinaryService = ServiceFactory.getCloudinaryService();
        authService = ServiceFactory.getAuthService();
        userService = ServiceFactory.getUserService();
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            UserDTO userDTO = authService.getUser(req);

            log.info("Редактирование фотографии пользователя - {}", userDTO.getLogin());

            /*
            Проверка, что у юзера на default фотку
             */
            String oldUrl = userDTO.getAvatarUrl();
            if (oldUrl != null) req.setAttribute("oldPhotoId", oldUrl);

            /*
            Юрл, где сохранена новая аватарка юзера
             */
            String url = cloudinaryService.uploadPhoto(req, oldUrl != null);

            log.info("Новая аватарка - {}", url);

             /*
             Изменение url в бд, а также обновление юзера в сессии
             */
            UserDTO changedUser = userService.updatePhotoUrl(userDTO, url, userDTO.getId());

            authService.updateUser(req, changedUser);
            ajaxUtil.senderRespSuccessTitle(resp, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            ajaxUtil.senderErrorMessage("Не удалось обновить аватарку", resp);
        }


    }
}
