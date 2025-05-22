package com.api.exchangerate.controller;

import com.api.exchangerate.model.response.ExchangeRateHistoryItemResponse;
import com.api.exchangerate.model.response.ExchangeRateHistoryResponse;
import com.api.exchangerate.model.response.PageData;
import com.api.exchangerate.service.business.ExchangeRateHistoryBusinessService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExchangeRateHistoryController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
class ExchangeRateHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateHistoryBusinessService businessService;

    @Test
    void exchangeCurrency_returnsOk() throws Exception {
        // Given
        String transactionId = UUID.randomUUID().toString();
        ExchangeRateHistoryItemResponse item = new ExchangeRateHistoryItemResponse(
                transactionId,
                Currency.getInstance("USD"),
                Currency.getInstance("TRY"),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(3500),
                LocalDateTime.now()
        );
        PageData page = new PageData(1, 1, 0, 10);
        ExchangeRateHistoryResponse mockResponse = new ExchangeRateHistoryResponse(List.of(item), page);
        Mockito.when(businessService.filterExchangeRateHistory(any()))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/exchange-rate/history")
                        .param("transactionId", transactionId)
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].transactionId").value(transactionId))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1));
    }

}
