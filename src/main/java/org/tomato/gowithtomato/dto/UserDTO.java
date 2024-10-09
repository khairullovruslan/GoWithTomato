package org.tomato.gowithtomato.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.tomato.gowithtomato.validator.annotations.ValidLogin;
import org.tomato.gowithtomato.validator.annotations.ValidPassword;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{

    @ValidLogin
    private String login;
    @ValidPassword
    private String password;
    @Email
    private String email;
    private String phoneNumber;
}
