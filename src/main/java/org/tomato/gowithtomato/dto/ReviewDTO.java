package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.validator.annotations.ValidRating;
import org.tomato.gowithtomato.validator.annotations.ValidReviewDescription;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewDTO {
    private Long id;
    private UserDTO owner;
    private TripDTO trip;

    @ValidRating
    @JsonProperty("rating")
    private int rating;

    @ValidReviewDescription
    @JsonProperty("reviewText")
    private String description;
}
