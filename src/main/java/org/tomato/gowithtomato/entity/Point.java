package org.tomato.gowithtomato.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Point {
    private Long id;
    private Double lat;
    private Double lng;
    private String name;
    private String country;
    private String state;
    private String osmValue;
}
