package org.tomato.gowithtomato.service.impl;

import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.factory.DaoFactory;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.mapper.TripMapper;
import org.tomato.gowithtomato.service.RouteService;
import org.tomato.gowithtomato.service.TripService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TripServiceImpl implements TripService {
    private final static TripServiceImpl INSTANCE = new TripServiceImpl();
    private final TripDAO tripDAO;
    private final TripMapper tripMapper;
    private final RouteService routeService;

    private TripServiceImpl() {
        tripDAO = DaoFactory.getTripDAO();
        tripMapper = TripMapper.getInstance();
        routeService = ServiceFactory.getRouteService();
    }

    public static TripServiceImpl getInstance() {
        return INSTANCE;
    }

    public Long saveTrip(UserDTO userDTO, TripDTO tripDTO, Long id) {
        tripDTO.setRoute(routeService.findById(id));
        return tripDAO.saveWithRouteId(tripMapper.convertDTOToTrip(tripDTO), id, userDTO.getId()).getId();
    }

    @Override

    public List<TripDTO> findByFilter(Map<String, String> filter) {
        List<Trip> trips = tripDAO.findAllByFilter(filter);
        return trips.stream().map(tripMapper::convertTripToDTO).toList();
    }

    @Override

    public TripDTO findById(Long id) {
        Optional<Trip> trip = tripDAO.findById(id);
        if (trip.isPresent()) return tripMapper.convertTripToDTO(trip.get());
        throw new RuntimeException();
    }

    @Override

    public Long getCountPage(Map<String, String> filter) {
        return tripDAO.getCountPage(filter);
    }

    @Override

    public void cancelTrip(Long id) {
        tripDAO.cancelTrip(id);
    }

    @Override

    public long getCountByUserId(Long id) {
        return tripDAO.getCountByUserId(id);
    }
}
