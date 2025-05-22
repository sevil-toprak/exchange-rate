package com.api.exchangerate.service.impl;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.constants.ErrorMessages;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final BuildRequestService buildRequestService;

    @Override
    public ExchangeRateResponse exchangeCurrency(ExchangeRateRequest request) {
        ExchangeApiConvertResponse apiResponse = buildRequestService.buildExchangeCurrencyRequest(request);
        validateResponseNotNull(apiResponse);
        return buildExchangeCurrencyResponse(apiResponse);
    }

    @Override
    public ConvertRateResponse convertCurrency(ConvertRateRequest request) {
        ExchangeApiConvertResponse apiResponse = buildRequestService.buildConvertCurrencyRequest(request);
        validateResponseNotNull(apiResponse);
        String transactionId = TransactionIdGeneratorUtil.generateTransactionId();
        return buildConvertCurrencyResponse(transactionId, apiResponse);
    }

    @Override
    public BulkConvertRateResponse bulkConvertCurrency(List<ConvertRateRequest> requestList) {
        List<ConvertRateResponse> responseList = new ArrayList<>();
        int failedCount = 0;
        for (ConvertRateRequest request : requestList) {
            try {
                ConvertRateResponse convertRateResponse = convertCurrency(request);
                responseList.add(convertRateResponse);
            } catch (Exception e) {
                failedCount ++;
                log.error("Bulk process error when integrating with external api service", e);
            }
        }

        return new BulkConvertRateResponse(
                responseList,
                requestList.size(),
                requestList.size() - failedCount,
                failedCount
        );
    }

    private void validateResponseNotNull(ExchangeApiConvertResponse apiResponse) {
        if (Objects.isNull(apiResponse) || !apiResponse.success()) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR,
                    ErrorMessages.API_INTEGRATION_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ExchangeRateResponse buildExchangeCurrencyResponse(ExchangeApiConvertResponse apiResponse) {
        return new ExchangeRateResponse(
                apiResponse.query().from(),
                apiResponse.query().to(),
                BigDecimal.valueOf(apiResponse.result()));
    }

    private ConvertRateResponse buildConvertCurrencyResponse(String transactionId, ExchangeApiConvertResponse apiResponse) {
        return new ConvertRateResponse(
                transactionId,
                Currency.getInstance(apiResponse.query().from()),
                Currency.getInstance(apiResponse.query().to()),
                BigDecimal.valueOf(apiResponse.result()),
                BigDecimal.valueOf(apiResponse.query().amount()));
    }
}
