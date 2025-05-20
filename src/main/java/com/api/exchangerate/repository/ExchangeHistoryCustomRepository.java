package com.api.exchangerate.repository;

import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import org.springframework.data.domain.Page;

public interface ExchangeHistoryCustomRepository {

    // this class can be used for complicated queries like filter, including null check, etc.

    Page<ExchangeHistory> filterExchangeRateHistory(ExchangeRateHistoryRequest request);
}

