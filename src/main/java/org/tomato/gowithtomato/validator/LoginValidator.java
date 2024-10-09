package org.tomato.gowithtomato.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.exception.UserNotFoundException;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.validator.annotations.ValidLogin;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        try {
            UserService.getInstance().findUserByLogin(login);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пользватель с таким логином уже зарегистрирован")
                    .addConstraintViolation();
            return false;
        }
        catch (UserNotFoundException e){
            return true;
        }
    }


}