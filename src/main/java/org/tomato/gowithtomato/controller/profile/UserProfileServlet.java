package org.tomato.gowithtomato.controller.profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.ReviewService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.util.PropertiesUtil;

import java.io.IOException;
import java.util.List;


/*
Профиль
 */
@Slf4j
@WebServlet("/profile")
public class UserProfileServlet extends BaseServlet {
    private UserService userService;

    private AuthService authService;

    private TripService tripService;

    private ReviewService reviewService;


    @Override
    public void init() {
        super.init();
        userService = ServiceFactory.getUserService();
        authService = ServiceFactory.getAuthService();
        tripService = ServiceFactory.getTripService();
        reviewService = ServiceFactory.getReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("u");


        log.info("Проверка, чью страницу отобразить");

        if (name == null && authService.authorizationCheck(req)) {
            req.setAttribute("isOwner", true);
            req.setAttribute("user", authService.getUser(req));
        } else if (name != null) {
            req.setAttribute("user", userService.findUserByLogin(name));
        } else {
            throw new UnauthorizedException("Нет доступа к странице");
        }


        UserDTO curUserProfile = (UserDTO) req.getAttribute("user");

        log.info("Проверка, какую аватарку брать у пользователя - {}", curUserProfile.getLogin());

        if (curUserProfile.getAvatarUrl() == null) {
            req.setAttribute("photo", PropertiesUtil.getInstance().get("profile.default.photo.url"));
        } else {
            req.setAttribute("photo", curUserProfile.getAvatarUrl());
        }

        List<ReviewDTO> reviews = reviewService.findByTripOwnerId(curUserProfile.getId());
        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        int pageSize = Integer.parseInt(PropertiesUtil.getInstance().get("profile.page.size"));


        req.setAttribute("tripCount", tripService.getCountByUserId(curUserProfile.getId()));
        req.setAttribute("reviewCount", reviews.size());
        req.setAttribute("rating",
                Math.round(
                        reviews.stream().mapToInt(ReviewDTO::getRating).sum() * 1.0 / reviews.size() * 100) / 100.0);
        req.setAttribute("reviewList", reviews.stream().skip((long) (page - 1) * pageSize).limit(pageSize).toList());
        req.setAttribute("page", page);
        req.setAttribute("totalPages", Math.ceil(reviews.size() * 1.0 / pageSize));

        req.getRequestDispatcher("/WEB-INF/templates/profile.jsp").forward(req, resp);

    }
}
