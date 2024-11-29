package org.tomato.gowithtomato.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String login;
    private String email;
    private String phoneNumber;
}
