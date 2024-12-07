package org.tomato.gowithtomato.service.impl;

import org.tomato.gowithtomato.dao.daoInterface.ReviewDAO;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.factory.DaoFactory;
import org.tomato.gowithtomato.mapper.ReviewMapper;
import org.tomato.gowithtomato.service.ReviewService;

import java.util.List;
import java.util.Optional;

public class ReviewServiceImpl implements ReviewService {
    private static final ReviewServiceImpl INSTANCE = new ReviewServiceImpl();
    private final ReviewDAO reviewDAO;
    private final ReviewMapper reviewMapper;

    private ReviewServiceImpl() {

        reviewDAO = DaoFactory.getReviewDAO();
        reviewMapper = ReviewMapper.getInstance();

    }

    public static ReviewServiceImpl getInstance() {
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
