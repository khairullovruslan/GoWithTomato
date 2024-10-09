package org.tomato.gowithtomato.dto;

import lombok.*;
import org.tomato.gowithtomato.validator.annotations.ValidPassword;

@Getter
@Setter
@Builder
public class UserDTO{
    private String login;

    @ValidPassword
    private String password;
    private String email;
    private String phoneNumber;
}
