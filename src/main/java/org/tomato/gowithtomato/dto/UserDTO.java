package org.tomato.gowithtomato.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class UserDTO{
    private String login;
    private String password;
    private String email;
    private String phoneNumber;
}
