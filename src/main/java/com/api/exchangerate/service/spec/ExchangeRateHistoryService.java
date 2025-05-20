package com.api.exchangerate.service.spec;

import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.request.CreateExchangeRateHistoryRequest;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import org.springframework.data.domain.Page;

public interface ExchangeRateHistoryService {

    void createExchangeHistory(CreateExchangeRateHistoryRequest request);

    Page<ExchangeHistory> filterExchangeRateHistory(ExchangeRateHistoryRequest request);
}
