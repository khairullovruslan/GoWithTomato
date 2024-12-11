package org.tomato.gowithtomato.validator.auth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.validator.annotations.auth.ValidPhoneNumber;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();
        if (phoneNumber == null) {
            errorMessages.add("Номер не может быть пустым.");
        } else {
            if (phoneNumber.charAt(0) != '+') {
                errorMessages.add("Телефонный номер должен начинаться с '+'");
            }
            String regex = "^\\+\\d{1,3}\\d{9,15}$";
            if (!phoneNumber.matches(regex)) {
                errorMessages.add("Неверный формат номера телефона");
            }
        }
        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String message : errorMessages) {
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
            }
            return false;
        }

        return true;

    }


}