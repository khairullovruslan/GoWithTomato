package org.tomato.gowithtomato.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tomato.gowithtomato.dto.route.RouteDTO;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.validator.annotations.trip.ValidTripAvailableSeats;
import org.tomato.gowithtomato.validator.annotations.trip.ValidTripDateTime;
import org.tomato.gowithtomato.validator.annotations.trip.ValidTripPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {
    private Long id;

    private RouteDTO route;

    @JsonProperty("tripDateTime")
    @ValidTripDateTime
    private LocalDateTime tripDateTime;

    @JsonProperty("availableSeats")
    @ValidTripAvailableSeats
    private int availableSeats;

    @JsonProperty("price")
    @ValidTripPrice
    private BigDecimal price;

    private TripStatus status;

    private String tripDateTimeFormatted;
}
