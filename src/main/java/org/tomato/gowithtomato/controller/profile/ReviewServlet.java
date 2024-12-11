package org.tomato.gowithtomato.controller.profile;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.AuthService;
import org.tomato.gowithtomato.service.ReviewService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.AjaxUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/*
Отображение отзыва поездки
 */
@Slf4j
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
        tripService = ServiceFactory.getTripService();
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        reviewService = ServiceFactory.getReviewService();
        authService = ServiceFactory.getAuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        UserDTO user = authService.getUser(req);
        TripDTO tripDTO = tripService.findById(Long.valueOf(req.getParameter("trip")));
        Optional<ReviewDTO> reviewDTO = reviewService.findByUserAndTripId(tripDTO.getId(), user.getId());

        /*
        Если reviewDto == null, то в html выведется форма, иначе подставятся значения
         */
        req.setAttribute("review", reviewDTO.orElse(null));
        req.setAttribute("trip", tripDTO);

        req.getRequestDispatcher("/WEB-INF/templates/review.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ReviewDTO reviewDTO = objectMapper.readValue(req.getInputStream(), ReviewDTO.class);
        Set<ConstraintViolation<ReviewDTO>> violations = validator.validate(reviewDTO);

        log.info("Создание нового отзыва");

        if (!violations.isEmpty()) {
            log.error("Пользователь ввел невалидные данные!");
            ajaxUtil.senderErrorMessage(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")), resp);
            return;
        }

        UserDTO user = authService.getUser(req);
        TripDTO tripDTO = tripService.findById(Long.valueOf(req.getParameter("trip")));

        reviewDTO.setOwner(user);
        reviewDTO.setTrip(tripDTO);

        /*
        Проверка на то, что юзер не оставлял уже отзыв
         */
        if (!reviewService.leftAReview(tripDTO.getId(), user.getId())) {
            reviewService.save(reviewDTO);
        }
        ajaxUtil.senderRespUrl("%s/review?trip=%d".formatted(req.getContextPath(), tripDTO.getId()), resp);


    }
}
