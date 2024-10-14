package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dao.TripDAOImpl;
import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.mapper.TripMapper;

public class TripService {
    private final static TripService INSTANCE = new TripService();
    private final TripDAOImpl tripDAO;
    private final TripMapper tripMapper;
    private TripService(){
        tripDAO = TripDAOImpl.getInstance();
        tripMapper = TripMapper.getInstance();
    }

    public static TripService getInstance() {
        return INSTANCE;
    }
    public TripDTO saveTrip(TripDTO tripDTO, Long id){
        return tripMapper.convertTripToDTO(tripDAO.save(tripMapper.convertDTOToTrip(tripDTO), id));
    }
}
