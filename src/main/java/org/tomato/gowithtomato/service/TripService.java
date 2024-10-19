package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dao.TripDAOImpl;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.mapper.TripMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TripService {
    private final static TripService INSTANCE = new TripService();
    private final TripDAOImpl tripDAO;
    private final TripMapper tripMapper;
    private final RouteService routeService;
    private TripService(){
        tripDAO = TripDAOImpl.getInstance();
        tripMapper = TripMapper.getInstance();
        routeService = RouteService.getInstance();
    }

    public static TripService getInstance() {
        return INSTANCE;
    }

    public void saveTrip(UserDTO userDTO, TripDTO tripDTO, Long id){
        tripDTO.setOwner(userDTO);
        tripDTO.setRoute(routeService.findById(id));
        tripDAO.save(tripMapper.convertDTOToTrip(tripDTO), id);
    }
    public List<TripDTO> findAll(){
        List<Trip> trips = tripDAO.findAll();
        return trips.stream().map(tripMapper::convertTripToDTO).toList();
    }

    public List<TripDTO> findByFilter(Map<String, String> filter) {
        List<Trip> trips = tripDAO.findAllByFilter(filter);
        return trips.stream().map(tripMapper::convertTripToDTO).toList();
    }
    public TripDTO findById(Long id){
        Optional<Trip> trip = tripDAO.findById(id);
        if(trip.isPresent()) return tripMapper.convertTripToDTO(trip.get());
        throw new RuntimeException();
    }
}
