package org.tomato.gowithtomato.entity;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private Long id;
    private String departurePoint;
    private String destinationPoint;
    private double distance;
}
