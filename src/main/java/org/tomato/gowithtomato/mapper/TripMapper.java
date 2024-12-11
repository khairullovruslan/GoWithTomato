package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.factory.DaoFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TripMapper implements RowMapper<Trip> {
    private final static TripMapper INSTANCE = new TripMapper();
    private final RouteDAO routeDAO;


    private final RouteMapper routeMapper;

    private TripMapper() {
        routeDAO = DaoFactory.getRouteDAO();
        routeMapper = RouteMapper.getInstance();

    }

    public static TripMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Trip mapRow(ResultSet result) throws SQLException {
        Optional<Route> route = routeDAO.findById(result.getLong("route_id"));
        if (route.isEmpty()) {
            throw new DaoException("Route are null");
        }
        return Trip.builder()
                .id(result.getLong("id"))
                .price(result.getBigDecimal("price"))
                .availableSeats(result.getInt("available_seats"))
                .tripDateTime(result.getTimestamp("trip_date_time").toLocalDateTime())
                .status(TripStatus.valueOf(result.getString("status")))
                .route(route.get())
                .build();
    }

    public Trip convertDTOToTrip(TripDTO tripDTO) {
        return Trip
                .builder()
                .id(tripDTO.getId())
                .route(routeMapper.convertDTOToRoute(tripDTO.getRoute()))
                .tripDateTime(tripDTO.getTripDateTime())
                .price(tripDTO.getPrice())
                .availableSeats(tripDTO.getAvailableSeats())
                .status(tripDTO.getStatus())
                .build();


    }

    public TripDTO convertTripToDTO(Trip trip) {
        return TripDTO
                .builder()
                .id(trip.getId())
                .route(routeMapper.convertRouteToDTO(trip.getRoute()))
                .tripDateTime(trip.getTripDateTime())
                .price(trip.getPrice())
                .availableSeats(trip.getAvailableSeats())
                .status(trip.getStatus())
                .build();
    }
}
