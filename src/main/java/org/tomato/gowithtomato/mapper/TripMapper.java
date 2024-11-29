package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.dao.impl.RouteDAOImpl;
import org.tomato.gowithtomato.dao.impl.UserDAOImpl;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TripMapper implements RowMapper<Trip> {
    private final static TripMapper INSTANCE = new TripMapper();
    private final UserDAO userDAO;
    private final RouteDAO routeDAO;

    private final UserMapper userMapper;

    private final RouteMapper routeMapper;

    private TripMapper() {
        userDAO = UserDAOImpl.getInstance();
        routeDAO = RouteDAOImpl.getInstance();
        routeMapper = RouteMapper.getInstance();
        userMapper = UserMapper.getInstance();

    }

    public static TripMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Trip mapRow(ResultSet result) throws SQLException {
        Optional<User> owner = userDAO.findById(result.getLong("user_id"));
        System.out.println(owner);
        Optional<Route> route = routeDAO.findById(result.getLong("route_id"));
        System.out.println("pu pu pu");
        if (owner.isEmpty() || route.isEmpty()) {
            throw new DaoException("Route or User are null");
        }

        return Trip.builder()
                .id(result.getLong("id"))
                .price(result.getBigDecimal("price"))
                .availableSeats(result.getInt("available_seats"))
                .tripDateTime(result.getTimestamp("trip_date_time").toLocalDateTime())
                .status(TripStatus.valueOf(result.getString("status")))
                .owner(owner.get())
                .route(route.get())
                .build();
    }

    public Trip convertDTOToTrip(TripDTO tripDTO){
        return Trip
                .builder()
                .id(tripDTO.getId())
                .owner(userMapper.convertDTOToUser(tripDTO.getOwner()))
                .route(routeMapper.convertDTOToRoute(tripDTO.getRoute()))
                .tripDateTime(tripDTO.getTripDateTime())
                .price(tripDTO.getPrice())
                .availableSeats(tripDTO.getAvailableSeats())
                .status(tripDTO.getStatus())
                .build();


    }
    public TripDTO convertTripToDTO(Trip trip){
        return TripDTO
                .builder()
                .id(trip.getId())
                .owner(userMapper.convertUserToDTO(trip.getOwner()))
                .route(routeMapper.convertRouteToDTO(trip.getRoute()))
                .tripDateTime(trip.getTripDateTime())
                .price(trip.getPrice())
                .availableSeats(trip.getAvailableSeats())
                .status(trip.getStatus())
                .build();
    }
}
