package com.api.exchangerate.service.impl;

import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.request.CreateExchangeRateHistoryRequest;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import com.api.exchangerate.repository.ExchangeHistoryRepository;
import com.api.exchangerate.service.spec.ExchangeRateHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExchangeRateHistoryServiceImpl implements ExchangeRateHistoryService {

    private final ExchangeHistoryRepository repository;

    @Override
    @Transactional
    public void createExchangeHistory(CreateExchangeRateHistoryRequest request) {
        ExchangeHistory entity = ExchangeHistory.builder()
                .transactionId(request.transactionId())
                .sourceCurrencyCode(request.source())
                .targetCurrencyCode(request.target())
                .sourceAmount(request.amount())
                .calculatedAmount(request.calculatedAmount())
                .type(request.type())
                .build();

        repository.save(entity);
    }

    @Override
    public Page<ExchangeHistory> filterExchangeRateHistory(ExchangeRateHistoryRequest request) {
        return repository.filterExchangeRateHistory(request);
    }
}
