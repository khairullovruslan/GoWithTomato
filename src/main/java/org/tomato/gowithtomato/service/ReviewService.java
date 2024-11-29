package org.tomato.gowithtomato.service;

import org.tomato.gowithtomato.dao.daoInterface.ReviewDAO;
import org.tomato.gowithtomato.dao.impl.ReviewDAOImpl;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.mapper.ReviewMapper;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private static final ReviewService INSTANCE = new ReviewService();
    private final ReviewDAO reviewDAO;
    private final ReviewMapper reviewMapper;

    private ReviewService() {

        reviewDAO = ReviewDAOImpl.getInstance();
        reviewMapper = ReviewMapper.getInstance();

    }

    public static ReviewService getInstance() {
        return INSTANCE;
    }

    public boolean leftAReview(long tripId, Long userId) {
        return reviewDAO.searchForUserInReviews(tripId, userId);
    }

    public void save(ReviewDTO tripDTO) {
        reviewDAO.save(reviewMapper.convertDTOToReview(tripDTO));
    }

    public Optional<ReviewDTO> findByUserAndTripId(long tripId, Long userId) {
        return reviewDAO.findByUserAndTripId(userId, tripId).map(reviewMapper::convertReviewToDto);
    }

    public List<ReviewDTO> findByTripOwnerId(Long id) {
        return reviewDAO.findByTripOwnerId(id).stream().map(reviewMapper::convertReviewToDto).toList();
    }
}
