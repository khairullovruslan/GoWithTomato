package org.tomato.gowithtomato.controller.profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.CloudinaryService;

import java.io.IOException;

@WebServlet("/profile/upload")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class UpdatePhotoWithCloudinaryServlet extends BaseServlet {
    private CloudinaryService cloudinaryService;


    @Override
    public void init() {
        super.init();
        cloudinaryService = ServiceFactory.getCloudinaryService();
    }

    // todo сохранение url в бд user
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            String url = cloudinaryService.uploadPhoto(req);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
