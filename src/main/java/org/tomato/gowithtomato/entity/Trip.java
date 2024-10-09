package org.tomato.gowithtomato.entity;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    private Long id;
    private User owner;
    private Route route;
    private LocalDateTime tripDateTime;
    private int availableSeats;
    private BigDecimal price;
    private TripStatus status;
}
