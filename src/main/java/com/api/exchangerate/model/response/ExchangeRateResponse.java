package com.api.exchangerate.model.response;


import java.math.BigDecimal;

public record ExchangeRateResponse(
        String source,
        String target,
        BigDecimal rate
) { }
