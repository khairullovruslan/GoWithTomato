package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dao.TripParticipantsDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.mapper.UserMapper;

import java.util.List;

public class TripParticipantsService {
    private final static TripParticipantsService INSTANCE = new TripParticipantsService();
    private final UserMapper userMapper;
    private final TripParticipantsDAOImpl tripParticipantsDAO;
    private TripParticipantsService(){
        userMapper = UserMapper.getInstance();
        tripParticipantsDAO = TripParticipantsDAOImpl.getInstance();
    }

    public static TripParticipantsService getInstance() {
        return INSTANCE;
    }
    public List<UserDTO> findUsersByTripId(Long id){
        List<User> users = tripParticipantsDAO.findUsersByTripId(id);
        return users.stream().map(userMapper::convertUserToDTO).toList();
    }
    public void save(Long tripId, Long userId){
        tripParticipantsDAO.save(tripId, userId);
    }
}
