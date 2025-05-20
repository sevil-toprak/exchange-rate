package com.api.exchangerate.model.request;

import com.api.exchangerate.exception.constants.ErrorMessages;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertRateRequest {

    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    private Currency source;

    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    private Currency target;

    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_AMOUNT_PARAMETER)
    private BigDecimal amount;

}
