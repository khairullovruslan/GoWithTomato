package org.tomato.gowithtomato.controller.profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.ReviewService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.service.UserService;

import java.io.IOException;
import java.util.List;


@WebServlet("/profile")
public class UserProfileServlet extends BaseServlet {
    private UserService userService;

    private AuthService authService;

    private TripService tripService;

    private ReviewService reviewService;


    @Override
    public void init() {
        super.init();
        userService = (UserService) this.getServletContext().getAttribute("userService");
        authService = (AuthService) this.getServletContext().getAttribute("authService");
        tripService = TripService.getInstance();
        reviewService = ReviewService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final int PAGE_SIZE = 4;
        String name = req.getParameter("u");

        if (name == null && authService.authorizationCheck(req)) {

            req.setAttribute("isOwner", true);
            req.setAttribute("user", req.getSession().getAttribute("user"));

        } else if (name != null) {

            req.setAttribute("user", userService.findUserByLogin(name));

        } else {

            throw new UnauthorizedException("Нет доступа к странице");
        }


        UserDTO curUserProfile = (UserDTO) req.getAttribute("user");
        req.setAttribute("tripCount", tripService.getCountByUserId(curUserProfile.getId()));
        List<ReviewDTO> reviews = reviewService.findByTripOwnerId(curUserProfile.getId());
        req.setAttribute("reviewCount", reviews.size());
        req.setAttribute("rating",
                Math.round(
                        reviews.stream().mapToInt(ReviewDTO::getRating).sum() * 1.0 / reviews.size() * 100
                )
                        / 100.0);
        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));

        req.setAttribute("reviewList", reviews.stream().skip((long) (page - 1) * PAGE_SIZE).limit(PAGE_SIZE).toList());
        req.setAttribute("page", page);
        req.setAttribute("totalPages", Math.ceil(reviews.size() * 1.0 / PAGE_SIZE));

        req.getRequestDispatcher("templates/profile.jsp").forward(req, resp);

    }
}
