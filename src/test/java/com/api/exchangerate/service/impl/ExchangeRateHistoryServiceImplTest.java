package com.api.exchangerate.service.impl;

import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.enums.HistoryType;
import com.api.exchangerate.model.request.CreateExchangeRateHistoryRequest;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import com.api.exchangerate.repository.ExchangeHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExchangeRateHistoryServiceImplTest {

    @Mock
    private ExchangeHistoryRepository repository;

    @InjectMocks
    private ExchangeRateHistoryServiceImpl service;

    @Test
    void testCreateExchangeHistory_shouldSaveEntity() {
        // Given
        String transactionId = UUID.randomUUID().toString();
        CreateExchangeRateHistoryRequest request = new CreateExchangeRateHistoryRequest(
                transactionId,
                Currency.getInstance("USD"),
                Currency.getInstance("TRY"),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(3500),
                HistoryType.CONVERT
        );

        // When
        service.createExchangeHistory(request);

        // Then
        ArgumentCaptor<ExchangeHistory> captor = ArgumentCaptor.forClass(ExchangeHistory.class);
        verify(repository).save(captor.capture());

        ExchangeHistory saved = captor.getValue();
        assertEquals(transactionId, saved.getTransactionId());
        assertEquals(Currency.getInstance("USD"), saved.getSourceCurrencyCode());
        assertEquals(Currency.getInstance("TRY"), saved.getTargetCurrencyCode());
        assertEquals(BigDecimal.valueOf(100), saved.getSourceAmount());
        assertEquals(BigDecimal.valueOf(3500), saved.getCalculatedAmount());
        assertEquals(HistoryType.CONVERT, saved.getType());
    }

    @Test
    void testFilterExchangeRateHistory_shouldReturnPagedData() {
        // Given
        String transactionId = UUID.randomUUID().toString();
        ExchangeRateHistoryRequest request = new ExchangeRateHistoryRequest();
        ExchangeHistory history = ExchangeHistory.builder()
                .transactionId(transactionId)
                .sourceCurrencyCode(Currency.getInstance("USD"))
                .targetCurrencyCode(Currency.getInstance("TRY"))
                .sourceAmount(BigDecimal.valueOf(100))
                .calculatedAmount(BigDecimal.valueOf(3200))
                .type(HistoryType.CONVERT)
                .build();

        Page<ExchangeHistory> expectedPage = new PageImpl<>(List.of(history), PageRequest.of(0, 10), 1);

        when(repository.filterExchangeRateHistory(request)).thenReturn(expectedPage);

        // When
        Page<ExchangeHistory> result = service.filterExchangeRateHistory(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(transactionId, result.getContent().getFirst().getTransactionId());
        verify(repository).filterExchangeRateHistory(request);
    }
}
