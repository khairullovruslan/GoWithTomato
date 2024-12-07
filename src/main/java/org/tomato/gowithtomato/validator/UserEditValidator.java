package org.tomato.gowithtomato.validator;

import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserEditDTO;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserEditValidator {

    private final UserService userService;


    public UserEditValidator() {
        this.userService = ServiceFactory.getUserService();
    }

    public List<String> isValid(UserDTO origin, UserEditDTO editUser) {
        List<String> exceptionMessages = new ArrayList<>();

        if (!origin.getEmail().equals(editUser.email())) {
            try {
                userService.findByEmail(editUser.email());
                exceptionMessages.add("Пользователь с таким email уже зарегистрирован");
            } catch (UserNotFoundException ignored) {
            }
        }

        if (!origin.getLogin().equals(editUser.login())) {
            try {
                userService.findUserByLogin(editUser.login());
                exceptionMessages.add("Пользователь с таким логином уже зарегистрирован");
            } catch (UserNotFoundException ignored) {
            }
        }
        return exceptionMessages;


    }
}
