package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dto.TripDTO;
import org.tomato.gowithtomato.dto.user.UserDTO;

import java.util.List;
import java.util.Map;

public interface TripService {

    Long saveTrip(UserDTO userDTO, TripDTO tripDTO, Long id);

    List<TripDTO> findByFilter(Map<String, String> filter);

    TripDTO findById(Long id);

    Long getCountPage(Map<String, String> filter);

    void cancelTrip(Long id);

    long getCountByUserId(Long id);

}
