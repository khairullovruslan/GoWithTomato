package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dto.user.UserDTO;

import java.util.List;

public interface TripParticipantsService {


    List<UserDTO> findUsersByTripId(Long id);

    void save(Long tripId, Long userId);
}
