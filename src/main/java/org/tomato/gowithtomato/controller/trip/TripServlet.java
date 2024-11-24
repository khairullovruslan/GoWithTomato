package org.tomato.gowithtomato.controller.trip;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.controller.common.BaseServlet;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.common.IncorrectRequestParametersException;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.service.CookieService;
import org.tomato.gowithtomato.service.ReviewService;
import org.tomato.gowithtomato.service.TripParticipantsService;
import org.tomato.gowithtomato.service.TripService;
import org.tomato.gowithtomato.util.AjaxUtil;
import org.tomato.gowithtomato.util.DateFormatter;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/trip/*")
public class TripServlet extends BaseServlet {
    private TripService tripService;
    private TripParticipantsService tripParticipantsService;
    private CookieService cookieService;
    private DateFormatter dateFormatter;
    private AjaxUtil ajaxUtil;
    private ReviewService reviewService;

    @Override
    public void init() {
        super.init();
        cookieService = (CookieService) this.getServletContext().getAttribute("cookieService");
        tripService = (TripService) this.getServletContext().getAttribute("tripService");
        tripParticipantsService = (TripParticipantsService) this.getServletContext().getAttribute("tripParticipantsService");
        dateFormatter = (DateFormatter) this.getServletContext().getAttribute("dateFormatter");
        ajaxUtil = (AjaxUtil) this.getServletContext().getAttribute("ajaxUtil");
        reviewService = (ReviewService) this.getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        long tripId = getTripId(req);
        TripDTO tripDTO = setFormattedDateTime(tripService.findById(tripId));
        List<UserDTO> members = tripParticipantsService.findUsersByTripId(tripId);
        UserDTO user;
        req.setAttribute("chosen", false);
        req.setAttribute("owner", false);
        req.setAttribute("leaveFeedback", false);
        try {
            user = cookieService.findUser(req);
            List<UserDTO> userDTOS = members.stream().filter(u -> u.getLogin().equals(user.getLogin())).toList();
            if (!userDTOS.isEmpty()) {
                req.setAttribute("chosen", true);
            }
            if (user.getId().equals(tripDTO.getOwner().getId())) {
                req.setAttribute("owner", true);
            }
            if (tripDTO.getStatus().equals(TripStatus.completed)){
                req.setAttribute("leaveFeedback", !reviewService.leftAReview(tripId, user.getId()));
            }

        } catch (UserNotFoundException ignored) {
        }
        req.setAttribute("tripInfo", generateTripInfo(tripDTO));
        req.setAttribute("trip", tripDTO);
        req.setAttribute("members", members);
        getServletContext().getRequestDispatcher("/templates/trip.jsp").forward(req, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && !pathInfo.isEmpty()) {
            String routeIdString = pathInfo.substring(1);
            long tripId = Long.parseLong(routeIdString);
            UserDTO user = cookieService.findUser(req);
            tripParticipantsService.save(tripId, user.getId());
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            throw new IncorrectRequestParametersException();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO user = cookieService.findUser(req);
        TripDTO tripDTO = setFormattedDateTime(tripService.findById(getTripId(req)));
        if (tripDTO.getOwner().getId().equals(user.getId())){
            tripService.cancelTrip(tripDTO.getId());
            ajaxUtil.senderRespUrl(req.getContextPath() + "/trip/" + tripDTO.getId(), resp);
        }

    }

    private TripDTO setFormattedDateTime(TripDTO tripDTO) {
        String formattedDate = dateFormatter.formatTripDateTime(tripDTO.getTripDateTime());
        tripDTO.setTripDateTimeFormatted(formattedDate);
        return tripDTO;
    }

    private String generateTripInfo(TripDTO tripDTO) {
        String fromCity = tripDTO.getRoute().getStart().getName();
        String toCity = tripDTO.getRoute().getFinish().getName();
        return dateFormatter.formatTripInfo(tripDTO.getTripDateTime(), fromCity, toCity);
    }

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