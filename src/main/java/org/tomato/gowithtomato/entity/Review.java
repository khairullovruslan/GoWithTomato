package org.tomato.gowithtomato.entity;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Long id;
    private User owner;
    private Trip trip;
    private int rating;
    private String description;
}
