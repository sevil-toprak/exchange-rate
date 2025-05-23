package com.api.exchangerate.annotation.validator;

import com.api.exchangerate.annotation.IsoCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class IsoCurrencyValidator
        implements ConstraintValidator<IsoCurrency, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        // It returns true because of the not null validation is on the request class.
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            Currency.getInstance(value.toUpperCase());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
