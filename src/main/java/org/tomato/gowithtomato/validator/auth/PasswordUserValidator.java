package org.tomato.gowithtomato.validator.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserPasswordEditDTO;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.util.PasswordUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PasswordUserValidator {
    private final UserService userService;

    public PasswordUserValidator() {
        this.userService = ServiceFactory.getUserService();
    }

    public List<String> validate(Validator validator,
                                 UserPasswordEditDTO userPasswordEditDTO,
                                 UserDTO userDTO) {
        Set<ConstraintViolation<UserPasswordEditDTO>> violations = validator.validate(userPasswordEditDTO);
        List<String> errorMessages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        String pwd = userService.getPasswordByLogin(userDTO.getLogin());
        if (!PasswordUtil.checkPassword(userPasswordEditDTO.oldPassword(), pwd)) {
            errorMessages.add("Неправильный пароль");
        }
        if (!userPasswordEditDTO.repeatPassword().equals(userPasswordEditDTO.newPassword())) {
            errorMessages.add("Пароли не совпадают");
        }
        return errorMessages;


    }
}
