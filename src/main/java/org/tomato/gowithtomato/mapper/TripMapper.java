package org.tomato.gowithtomato.mapper;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.User;


public class TripMapper {
    private static final TripMapper INSTANCE = new TripMapper();
    private final UserMapper userMapper;
    private final RouteMapper routeMapper ;

    private TripMapper(){
        userMapper = UserMapper.getInstance();
        routeMapper = RouteMapper.getInstance();
    }

    public static TripMapper getInstance() {
        return INSTANCE;
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
