package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dto.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    boolean leftAReview(long tripId, Long userId);


    void save(ReviewDTO tripDTO);

    Optional<ReviewDTO> findByUserAndTripId(long tripId, Long userId);

    List<ReviewDTO> findByTripOwnerId(Long id);
}
