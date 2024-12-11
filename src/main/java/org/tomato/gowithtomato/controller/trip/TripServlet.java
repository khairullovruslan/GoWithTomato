package org.tomato.gowithtomato.controller.trip;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.common.IncorrectRequestParametersException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.*;
import org.tomato.gowithtomato.util.AjaxUtil;
import org.tomato.gowithtomato.util.DateFormatter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Slf4j
@WebServlet("/trip/*")
public class TripServlet extends BaseServlet {
    private TripService tripService;
    private TripParticipantsService tripParticipantsService;
    private DateFormatter dateFormatter;
    private AjaxUtil ajaxUtil;
    private ReviewService reviewService;
    private UserService userService;
    private AuthService authService;

    @Override
    public void init() {
        super.init();
        tripService = ServiceFactory.getTripService();
        tripParticipantsService = ServiceFactory.getTripParticipantsService();
        dateFormatter = (DateFormatter) this.getServletContext().getAttribute("dateFormatter");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        reviewService = ServiceFactory.getReviewService();
        authService = ServiceFactory.getAuthService();
        userService = ServiceFactory.getUserService();

    }

    /*
    Вывод определенной поездки по id
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        long tripId = getTripId(req);
        log.info("Вывод страницы поездки по id = {}", tripId);

        TripDTO tripDTO = setFormattedDateTime(tripService.findById(tripId));
        List<UserDTO> members = tripParticipantsService.findUsersByTripId(tripId);
        UserDTO user = authService.getUser(req);

        /*
        Дефолтные значение
         */
        req.setAttribute("chosen", false);
        req.setAttribute("owner", false);
        req.setAttribute("leaveFeedback", false);

        Optional<UserDTO> userDTO = userService.getByTripId(tripDTO.getId());
        req.setAttribute("ownerTrip", userDTO.isPresent() ? userDTO.get() : null);

        if (user != null) {

            List<UserDTO> userDTOList = members.stream().filter(u -> u.getLogin().equals(user.getLogin())).toList();


            if (!userDTOList.isEmpty()) {
                req.setAttribute("chosen", true);
            }
            if (userDTO.isPresent() && user.getId().equals(userDTO.get().getId())) {
                req.setAttribute("owner", true);
            }
            if (tripDTO.getStatus().equals(TripStatus.completed)) {
                req.setAttribute("leaveFeedback", !reviewService.leftAReview(tripId, user.getId()));
            }
        }

        req.setAttribute("tripInfo", generateTripInfo(tripDTO));
        req.setAttribute("trip", tripDTO);
        req.setAttribute("members", members);

        getServletContext().getRequestDispatcher("/WEB-INF/templates/trip.jsp").forward(req, resp);

    }


    /*
    Бронь места в поездке
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();


        if (pathInfo != null && !pathInfo.isEmpty()) {
            String routeIdString = pathInfo.substring(1);
            long tripId = Long.parseLong(routeIdString);
            UserDTO user = authService.getUser(req);

            log.info("Бронь места в поездке - {} пользователем - {}", tripId, user.getId());

            tripParticipantsService.save(tripId, user.getId());

            resp.sendRedirect("%s/profile".formatted(req.getContextPath()));
        } else {
            throw new IncorrectRequestParametersException();
        }
    }

    /*
    Отмена поездки
    */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        UserDTO user = authService.getUser(req);
        TripDTO tripDTO = setFormattedDateTime(tripService.findById(getTripId(req)));

        Optional<UserDTO> userDTO = userService.getByTripId(tripDTO.getId());
        if (userDTO.isPresent() && userDTO.get().getId().equals(user.getId())) {
            tripService.cancelTrip(tripDTO.getId());
            ajaxUtil.senderRespUrl("%s/trip/%s".formatted(req.getContextPath(), tripDTO.getId()), resp);
        }
        ajaxUtil.senderErrorMessage("Недостаточно прав для отмены поездки", resp);

    }

    /*
    Форматирование времени у поездки
     */
    private TripDTO setFormattedDateTime(TripDTO tripDTO) {
        String formattedDate = dateFormatter.formatTripDateTime(tripDTO.getTripDateTime());
        tripDTO.setTripDateTimeFormatted(formattedDate);
        return tripDTO;
    }

    /*
    Генерация информации поездки для view
     */
    private String generateTripInfo(TripDTO tripDTO) {
        String fromCity = tripDTO.getRoute().getStart().getName();
        String toCity = tripDTO.getRoute().getFinish().getName();
        return dateFormatter.formatTripInfo(tripDTO.getTripDateTime(), fromCity, toCity);
    }

    /*
    Получение id поездки из url
     */
    private Long getTripId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            try {
                String tripIdString = pathInfo.substring(1);
                return Long.parseLong(tripIdString);
            } catch (NumberFormatException e) {
                log.error("Неверный формат идентификатора поездки: {}", pathInfo, e);
                throw new IncorrectRequestParametersException();
            }
        } else {
            throw new IncorrectRequestParametersException();
        }
    }

}