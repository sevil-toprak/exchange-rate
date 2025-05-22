package com.api.exchangerate.service.impl;

import com.api.exchangerate.config.ExchangeApiProperties;
import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.ExchangeApiConvertResponse;
import com.api.exchangerate.util.HttpRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildRequestServiceImplTest {

    @Mock
    private ExchangeApiProperties properties;

    @Mock
    private HttpRequester httpRequester;

    @InjectMocks
    private BuildRequestServiceImpl buildRequestService;

    @BeforeEach
    void setup() {
        when(properties.getUrl()).thenReturn("https://api.exchangeratesapi.io/");
        when(properties.getKey()).thenReturn("test-key");
    }

    @Test
    void testBuildExchangeCurrencyRequest_shouldReturnResponse() {
        // Given
        ExchangeRateRequest request = ExchangeRateRequest.builder()
                .source("USD")
                .target("TRY")
                .build();

        ExchangeApiConvertResponse expectedResponse = new ExchangeApiConvertResponse(
                true,
                "terms",
                "privacy",
                new ExchangeApiConvertResponse.Query("USD", "TRY", 1),
                new ExchangeApiConvertResponse.Info(1234567890L, 35.00),
                3500.0
        );

        when(httpRequester.get(any())).thenReturn(expectedResponse);

        // When
        ExchangeApiConvertResponse response = buildRequestService.buildExchangeCurrencyRequest(request);

        // Then
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("USD", response.query().from());
        assertEquals("TRY", response.query().to());
        assertEquals(3500.0, response.result());
        verify(httpRequester, times(1)).get(any());
    }

    @Test
    void testBuildConvertCurrencyRequest_shouldReturnResponse() {
        // Giiven
        ConvertRateRequest request = ConvertRateRequest.builder()
                .source("EUR")
                .target("USD")
                .amount(BigDecimal.valueOf(100))
                .build();

        ExchangeApiConvertResponse expectedResponse = new ExchangeApiConvertResponse(
                true,
                "terms",
                "privacy",
                new ExchangeApiConvertResponse.Query("EUR", "USD", 100),
                new ExchangeApiConvertResponse.Info(1234567890L, 0.8825),
                88.25
        );

        when(httpRequester.get(any())).thenReturn(expectedResponse);

        // When
        ExchangeApiConvertResponse response = buildRequestService.buildConvertCurrencyRequest(request);

        // Then
        assertNotNull(response);
        assertEquals("EUR", response.query().from());
        assertEquals("USD", response.query().to());
        assertEquals(88.25, response.result());
        verify(httpRequester, times(1)).get(any());
    }

    @Test
    void testCallConvertEndpoint_whenHttpFails_shouldThrowBaseException() {
        // Given
        ConvertRateRequest request = ConvertRateRequest.builder()
                .source("USD")
                .target("TRY")
                .amount(BigDecimal.valueOf(100))
                .build();

        when(httpRequester.get(any()))
                .thenThrow(new RuntimeException("Simulated timeout"));

        // When & Then
        BaseException exception = assertThrows(BaseException.class,
                () -> buildRequestService.buildConvertCurrencyRequest(request));

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        verify(httpRequester, times(1)).get(any());
    }
}
