package org.tomato.gowithtomato.entity;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private Long id;
    private int countTrips;
    private int sumRates;
    private User owner;
}

