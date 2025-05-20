package com.api.exchangerate.service.business;

import com.api.exchangerate.model.enums.HistoryType;
import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.CreateExchangeRateHistoryRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;
import com.api.exchangerate.service.spec.ExchangeRateHistoryService;
import com.api.exchangerate.service.spec.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateBusinessService {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateHistoryService historyService;

    public ExchangeRateResponse exchangeCurrency(ExchangeRateRequest request) {
        return exchangeRateService.exchangeCurrency(request);
    }

    public ConvertRateResponse convertCurrency(ConvertRateRequest request) {
        ConvertRateResponse response = exchangeRateService.convertCurrency(request);
        generateExchangeRateHistory(response);
        return response;
    }

    public BulkConvertRateResponse bulkConvertCurrency() {
        return exchangeRateService.bulkConvertCurrency();
    }

    private void generateExchangeRateHistory(ConvertRateResponse response) {
        CreateExchangeRateHistoryRequest historyRequest = new CreateExchangeRateHistoryRequest(
                response.transactionId(),
                response.source(),
                response.target(),
                response.amount(),
                response.calculatedAmount(),
                HistoryType.CONVERT);
        historyService.createExchangeHistory(historyRequest);
        log.info("Exchange History has been created successfully.");
    }
}
