package com.api.exchangerate.service.business;

import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import com.api.exchangerate.model.response.ExchangeRateHistoryItemResponse;
import com.api.exchangerate.model.response.ExchangeRateHistoryResponse;
import com.api.exchangerate.model.response.PageData;
import com.api.exchangerate.service.spec.ExchangeRateHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateHistoryBusinessServiceTest {

    @Mock
    private ExchangeRateHistoryService historyService;

    @InjectMocks
    private ExchangeRateHistoryBusinessService businessService;

    @Test
    void testFilterExchangeRateHistory_shouldReturnMappedResponse() {
        // Given
        String transactionId = UUID.randomUUID().toString();
        ExchangeRateHistoryRequest request = new ExchangeRateHistoryRequest();
        ExchangeHistory exchangeHistory = new ExchangeHistory();
        exchangeHistory.setTransactionId(transactionId);
        exchangeHistory.setSourceCurrencyCode(Currency.getInstance("USD"));
        exchangeHistory.setTargetCurrencyCode(Currency.getInstance("EUR"));
        exchangeHistory.setSourceAmount(BigDecimal.valueOf(100.0));
        exchangeHistory.setCalculatedAmount(BigDecimal.valueOf(88.0));
        exchangeHistory.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));

        List<ExchangeHistory> content = List.of(exchangeHistory);
        Page<ExchangeHistory> page = new PageImpl<>(content, PageRequest.of(0, 10), 1);

        when(historyService.filterExchangeRateHistory(request)).thenReturn(page);

        // When
        ExchangeRateHistoryResponse response = businessService.filterExchangeRateHistory(request);

        // Then
        assertNotNull(response);
        assertEquals(1, response.data().size());

        ExchangeRateHistoryItemResponse item = response.data().getFirst();
        assertEquals(transactionId, item.transactionId());
        assertEquals(Currency.getInstance("USD"), item.source());
        assertEquals(Currency.getInstance("EUR"), item.target());
        assertEquals(BigDecimal.valueOf(88.0), item.calculatedAmount());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), item.createdAt());

        PageData pageData = response.page();
        assertEquals(1, pageData.totalElements());
        assertEquals(1, pageData.totalPages());
        assertEquals(0, pageData.page());
        assertEquals(10, pageData.size());

        verify(historyService).filterExchangeRateHistory(request);
    }
}