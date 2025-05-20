package com.api.exchangerate.model.request;

import com.api.exchangerate.annotation.IsoCurrency;
import lombok.*;

import java.util.Currency;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest {

    //    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    @IsoCurrency
    private Currency source;

    //    @NotNull(message = ErrorMessages.INVALID_REQUEST_MISSING_CURRENCY_PARAMETER)
    @IsoCurrency
    private Currency target;

}
