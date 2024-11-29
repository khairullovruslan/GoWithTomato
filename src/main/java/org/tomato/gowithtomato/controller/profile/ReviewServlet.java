package org.tomato.gowithtomato.controller.profile;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.ReviewService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/review")
public class ReviewServlet extends BaseServlet {

    private TripService tripService;
    private ObjectMapper objectMapper;
    private AjaxUtil ajaxUtil;
    private ReviewService reviewService;

    private AuthService authService;


    @Override
    public void init() {
        super.init();
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        tripService = (TripService) this.getServletContext().getAttribute("tripService");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        reviewService = (ReviewService) this.getServletContext().getAttribute("reviewService");
        authService = (AuthService) this.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        UserDTO user = (UserDTO) req.getAttribute("user");
        TripDTO tripDTO = tripService.findById(Long.valueOf(req.getParameter("trip")));
        Optional<ReviewDTO> reviewDTO = reviewService.findByUserAndTripId(tripDTO.getId(), user.getId());
        req.setAttribute("review", reviewDTO.orElse(null));
        req.setAttribute("trip", tripDTO);
        req.getRequestDispatcher("/templates/review.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReviewDTO reviewDTO = objectMapper.readValue(req.getInputStream(), ReviewDTO.class);
        UserDTO user = authService.getUser(req);
        TripDTO tripDTO = tripService.findById(Long.valueOf(req.getParameter("trip")));
        reviewDTO.setOwner(user);
        reviewDTO.setTrip(tripDTO);
        if (!reviewService.leftAReview(tripDTO.getId(), user.getId())) {
            reviewService.save(reviewDTO);
        }
        ajaxUtil.senderRespUrl(req.getContextPath() + "/review?trip=%d".formatted(tripDTO.getId()), resp);


    }
}
