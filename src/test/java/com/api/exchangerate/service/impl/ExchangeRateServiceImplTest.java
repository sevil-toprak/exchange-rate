package com.api.exchangerate.service.impl;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeApiConvertResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;
import com.api.exchangerate.service.spec.BuildRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceImplTest {

    @Mock
    private BuildRequestService buildRequestService;

    @InjectMocks
    private ExchangeRateServiceImpl service;

    @Test
    void testExchangeCurrency_shouldReturnExchangeRateResponse() {
        // Given
        ExchangeRateRequest request = new ExchangeRateRequest("USD", "EUR");
        ExchangeApiConvertResponse response = createApiResponse(true, "USD", "EUR", 0.8825);

        when(buildRequestService.buildExchangeCurrencyRequest(request)).thenReturn(response);

        // When
        ExchangeRateResponse result = service.exchangeCurrency(request);

        // Then
        assertEquals("USD", result.source());
        assertEquals("EUR", result.target());
        assertEquals(BigDecimal.valueOf(0.8825), result.rate());
    }

    @Test
    void testConvertCurrency_shouldReturnConvertRateResponse() {
        // Given
        ConvertRateRequest request = ConvertRateRequest.builder()
                .source("USD")
                .target("TRY")
                .amount(BigDecimal.valueOf(100))
                .build();

        ExchangeApiConvertResponse response = createApiResponse(true, "USD", "TRY", 100, 3500);

        when(buildRequestService.buildConvertCurrencyRequest(request)).thenReturn(response);

        // When
        ConvertRateResponse result = service.convertCurrency(request);

        // Then
        assertEquals(Currency.getInstance("USD"), result.source());
        assertEquals(Currency.getInstance("TRY"), result.target());
        assertEquals(0, result.calculatedAmount().compareTo(BigDecimal.valueOf(3500)));
        assertEquals(0, result.amount().compareTo(BigDecimal.valueOf(100)));
        assertNotNull(result.transactionId());
    }

    @Test
    void testBulkConvertCurrency_shouldReturnAllResponses() {
        // Given
        ConvertRateRequest req1 = new ConvertRateRequest("USD", "TRY", BigDecimal.valueOf(100));
        ConvertRateRequest req2 = new ConvertRateRequest("EUR", "USD", BigDecimal.valueOf(50));

        ExchangeApiConvertResponse resp1 = createApiResponse(true, "USD", "TRY", 3500, 100);
        ExchangeApiConvertResponse resp2 = createApiResponse(true, "EUR", "USD", 55, 50);

        when(buildRequestService.buildConvertCurrencyRequest(req1)).thenReturn(resp1);
        when(buildRequestService.buildConvertCurrencyRequest(req2)).thenReturn(resp2);

        // When
        BulkConvertRateResponse result = service.bulkConvertCurrency(List.of(req1, req2));

        // Then
        assertEquals(2, result.count());
        assertEquals(2, result.rows().size());
    }

    @Test
    void testExchangeCurrency_shouldThrowException_whenApiResponseIsNullOrInvalid() {
        // Given
        ExchangeRateRequest request = new ExchangeRateRequest("USD", "TRY");
        ExchangeApiConvertResponse failedResponse = createApiResponse(false, "USD", "TRY", 0);

        when(buildRequestService.buildExchangeCurrencyRequest(request)).thenReturn(null);

        // When & Then
        BaseException ex1 = assertThrows(BaseException.class, () -> service.exchangeCurrency(request));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, ex1.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex1.getHttpStatus());

        when(buildRequestService.buildExchangeCurrencyRequest(request)).thenReturn(failedResponse);

        BaseException ex2 = assertThrows(BaseException.class, () -> service.exchangeCurrency(request));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, ex2.getCode());
    }

    private ExchangeApiConvertResponse createApiResponse(boolean success, String from, String to, double result) {
        return new ExchangeApiConvertResponse(
                success,
                "terms",
                "privacy",
                new ExchangeApiConvertResponse.Query(from, to, 1),
                new ExchangeApiConvertResponse.Info(1234567890L, 1.0),
                result
        );
    }

    private ExchangeApiConvertResponse createApiResponse(boolean success, String from, String to, double amount, double result) {
        return new ExchangeApiConvertResponse(
                success,
                "terms",
                "privacy",
                new ExchangeApiConvertResponse.Query(from, to, amount),
                new ExchangeApiConvertResponse.Info(1234567890L, 1.0),
                result
        );
    }
}
