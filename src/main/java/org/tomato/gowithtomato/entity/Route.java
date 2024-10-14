package org.tomato.gowithtomato.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private Long id;
    private User owner;
    private Point departurePoint;
    private Point destinationPoint;
    private List<Point> other;
    private double distance;
}
