package com.api.exchangerate.annotation;

import com.api.exchangerate.annotation.validator.IsoCurrencyValidator;
import com.api.exchangerate.exception.constants.ErrorMessages;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsoCurrencyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoCurrency {

    String message() default ErrorMessages.CURRENCY_CODE_NOT_FOUND;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}