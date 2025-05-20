package com.api.exchangerate.model.request;

import com.api.exchangerate.model.enums.HistoryType;

import java.math.BigDecimal;
import java.util.Currency;

public record CreateExchangeRateHistoryRequest(
        String transactionId,
        Currency source,
        Currency target,
        BigDecimal amount,
        BigDecimal calculatedAmount,
        HistoryType type
) {
}