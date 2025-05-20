package com.api.exchangerate.annotation.validator;

import com.api.exchangerate.annotation.IsoCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class IsoCurrencyValidator implements ConstraintValidator<IsoCurrency, Currency> {

    @Override
    public boolean isValid(Currency value, ConstraintValidatorContext ctx) {
        // Currency class is return if any currency code does not exist, like XXX, YYY, USDD
        return value != null && value.getCurrencyCode() != null;
    }
}
