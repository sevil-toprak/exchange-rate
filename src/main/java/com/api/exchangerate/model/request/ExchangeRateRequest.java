package com.api.exchangerate.model.request;

import com.api.exchangerate.annotation.IsoCurrency;
import com.api.exchangerate.exception.constants.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest {

    @NotBlank(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    @IsoCurrency
    private String source;

    @NotBlank(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    @IsoCurrency
    private String target;

}
