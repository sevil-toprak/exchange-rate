package com.api.exchangerate.model.response;

import java.util.List;

public record ExchangeRateHistoryResponse(
        List<ExchangeRateHistoryItemResponse> data,
        PageData page
) {}
