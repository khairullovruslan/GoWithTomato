package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.User;

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

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("reviewText")
    private String description;
}
