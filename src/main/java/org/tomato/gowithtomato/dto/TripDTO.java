package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.tomato.gowithtomato.entity.TripStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {
    private Long id;

    private UserDTO owner;

    private RouteDTO route;

    @JsonProperty("tripDateTime")
    private LocalDateTime tripDateTime;

    @JsonProperty("availableSeats")
    private int availableSeats;

    @JsonProperty("price")
    private BigDecimal price;

    private TripStatus status;
}
