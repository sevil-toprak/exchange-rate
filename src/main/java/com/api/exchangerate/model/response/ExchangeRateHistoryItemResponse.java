package com.api.exchangerate.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public record ExchangeRateHistoryItemResponse(
        String transactionId,
        Currency source,
        Currency target,
        BigDecimal sourceAmount,
        BigDecimal calculatedAmount,
        LocalDateTime createdAt
) {
}
