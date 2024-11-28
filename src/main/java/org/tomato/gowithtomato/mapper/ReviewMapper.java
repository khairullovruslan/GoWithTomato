package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.entity.Review;

public class ReviewMapper {

    private final static ReviewMapper INSTANCE = new ReviewMapper();
    private final UserMapper userMapper;
    private final TripMapper tripMapper;

    private ReviewMapper() {
        userMapper = UserMapper.getInstance();
        tripMapper = TripMapper.getInstance();
    }

    public static ReviewMapper getInstance() {
        return INSTANCE;
    }

    public Review convertDTOToReview(ReviewDTO reviewDTO) {
        return Review
                .builder()
                .rating(reviewDTO.getRating())
                .description(reviewDTO.getDescription())
                .owner(userMapper.convertDTOToUser(reviewDTO.getOwner()))
                .trip(tripMapper.convertDTOToTrip(reviewDTO.getTrip()))
                .build();
    }

    public ReviewDTO convertReviewToDto(Review review) {
        return ReviewDTO
                .builder()
                .rating(review.getRating())
                .description(review.getDescription())
                .owner(userMapper.convertUserToDTO(review.getOwner()))
                .trip(tripMapper.convertTripToDTO(review.getTrip())).build();
    }


}
