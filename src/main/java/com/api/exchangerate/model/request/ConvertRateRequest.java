package com.api.exchangerate.model.request;

import com.api.exchangerate.annotation.IsoCurrency;
import com.api.exchangerate.exception.constants.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertRateRequest {

    @NotBlank(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    @IsoCurrency
    private String source;

    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    @IsoCurrency
    private String target;

    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_AMOUNT_PARAMETER)
    private BigDecimal amount;

}
