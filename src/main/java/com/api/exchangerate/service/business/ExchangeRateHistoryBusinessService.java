package com.api.exchangerate.service.business;


import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import com.api.exchangerate.model.response.ExchangeRateHistoryItemResponse;
import com.api.exchangerate.model.response.ExchangeRateHistoryResponse;
import com.api.exchangerate.model.response.PageData;
import com.api.exchangerate.service.spec.ExchangeRateHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateHistoryBusinessService {

    private final ExchangeRateHistoryService historyService;

    public ExchangeRateHistoryResponse filterExchangeRateHistory(ExchangeRateHistoryRequest request) {
        Page<ExchangeHistory> pageResponse = historyService.filterExchangeRateHistory(request);

        List<ExchangeRateHistoryItemResponse> items = pageResponse.getContent().stream()
                .map(this::toItem)
                .toList();

        PageData page = new PageData(
                pageResponse.getTotalElements(),
                pageResponse.getTotalPages(),
                pageResponse.getNumber(),
                pageResponse.getSize()
        );

        return new ExchangeRateHistoryResponse(items, page);
    }

    private ExchangeRateHistoryItemResponse toItem(ExchangeHistory exchangeHistory) {
        return new ExchangeRateHistoryItemResponse(
                exchangeHistory.getTransactionId(),
                exchangeHistory.getSourceCurrencyCode(),
                exchangeHistory.getTargetCurrencyCode(),
                exchangeHistory.getSourceAmount(),
                exchangeHistory.getCalculatedAmount(),
                exchangeHistory.getCreatedAt()
        );
    }
}
