package com.api.exchangerate.model.response;

import java.math.BigDecimal;
import java.util.Currency;

public record ConvertRateResponse(
        String transactionId,
        Currency source,
        Currency target,
        BigDecimal calculatedAmount,
        BigDecimal amount
) {
}
