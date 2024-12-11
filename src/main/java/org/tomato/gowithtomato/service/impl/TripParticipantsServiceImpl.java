package org.tomato.gowithtomato.service.impl;

import org.tomato.gowithtomato.dao.daoInterface.m2m.TripParticipantsDAO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.factory.DaoFactory;
import org.tomato.gowithtomato.mapper.UserMapper;
import org.tomato.gowithtomato.service.TripParticipantsService;

import java.util.List;

public class TripParticipantsServiceImpl implements TripParticipantsService {
    private final static TripParticipantsServiceImpl INSTANCE = new TripParticipantsServiceImpl();
    private final UserMapper userMapper;
    private final TripParticipantsDAO tripParticipantsDAO;

    private TripParticipantsServiceImpl() {
        userMapper = UserMapper.getInstance();
        tripParticipantsDAO = DaoFactory.getTripParticipantsDAO();
    }

    public static TripParticipantsServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override

    public List<UserDTO> findUsersByTripId(Long id) {
        List<User> users = tripParticipantsDAO.findUsersByTripId(id);
        return users.stream().map(userMapper::convertUserToDTO).toList();
    }

    @Override

    public void save(Long tripId, Long userId) {
        tripParticipantsDAO.save(tripId, userId);
    }
}
