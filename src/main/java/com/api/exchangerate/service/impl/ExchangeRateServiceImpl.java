package com.api.exchangerate.service.impl;

import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeApiConvertResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;
import com.api.exchangerate.service.spec.BuildRequestService;
import com.api.exchangerate.service.spec.ExchangeRateService;
import com.api.exchangerate.util.TransactionIdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final BuildRequestService buildRequestService;

    @Override
    public ExchangeRateResponse exchangeCurrency(ExchangeRateRequest request) {
        ExchangeApiConvertResponse apiResponse = buildRequestService.buildExchangeCurrencyRequest(request);
        return buildExchangeCurrencyResponse(apiResponse);
    }

    @Override
    public ConvertRateResponse convertCurrency(ConvertRateRequest request) {
        ExchangeApiConvertResponse apiResponse = buildRequestService.buildConvertCurrencyRequest(request);
        String transactionId = TransactionIdGeneratorUtil.generateTransactionId();
        return buildConvertCurrencyResponse(transactionId, apiResponse);
    }

    @Override
    public BulkConvertRateResponse bulkConvertCurrency() {
        return null;
    }

    private static ExchangeRateResponse buildExchangeCurrencyResponse(ExchangeApiConvertResponse apiResponse) {
        return new ExchangeRateResponse(
                apiResponse.query().from(),
                apiResponse.query().to(),
                BigDecimal.valueOf(apiResponse.result()));
    }

    private static ConvertRateResponse buildConvertCurrencyResponse(String transactionId, ExchangeApiConvertResponse apiResponse) {
        return new ConvertRateResponse(
                transactionId,
                Currency.getInstance(apiResponse.query().from()),
                Currency.getInstance(apiResponse.query().to()),
                BigDecimal.valueOf(apiResponse.result()),
                BigDecimal.valueOf(apiResponse.query().amount()));
    }
}
