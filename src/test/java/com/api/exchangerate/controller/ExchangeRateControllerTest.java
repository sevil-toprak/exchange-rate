package com.api.exchangerate.controller;

import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;
import com.api.exchangerate.service.business.ExchangeRateBusinessService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExchangeRateController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateBusinessService businessService;

    @Test
    void exchangeCurrency_returnsOk() throws Exception {
        // Given
        Mockito.when(businessService.exchangeCurrency(any()))
                .thenReturn(new ExchangeRateResponse("USD", "TRY", BigDecimal.valueOf(35.00)));

        // When & Then
        mockMvc.perform(get("/api/exchange-rate/exchange")
                        .param("source", "USD")
                        .param("target", "TRY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("USD"))
                .andExpect(jsonPath("$.target").value("TRY"))
                .andExpect(jsonPath("$.rate").value(35.00));
    }

    @Test
    void convertCurrency_returnsOk() throws Exception {
        // Given
        Mockito.when(businessService.convertCurrency(any()))
                .thenReturn(new ConvertRateResponse(
                        "txn-001",
                        Currency.getInstance("USD"),
                        Currency.getInstance("EUR"),
                        BigDecimal.valueOf(88.25),
                        BigDecimal.valueOf(100)));

        // When & Then
        mockMvc.perform(get("/api/exchange-rate/convert")
                        .param("source", "USD")
                        .param("target", "EUR")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("txn-001"))
                .andExpect(jsonPath("$.source").value("USD"))
                .andExpect(jsonPath("$.target").value("EUR"))
                .andExpect(jsonPath("$.calculatedAmount").value(88.25))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void bulkConvertCurrency_returnsOk() throws Exception {
        // Given
        MockMultipartFile csv =
                new MockMultipartFile("file", "bulk.csv", "text/csv",
                        "source,target,amount\nUSD,TRY,100".getBytes());

        Mockito.when(businessService.bulkConvertCurrency(any()))
                .thenReturn(new BulkConvertRateResponse(List.of(), 1, 1, 2));

        // When & Then
        mockMvc.perform(multipart("/api/exchange-rate/bulk-convert").file(csv))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(1));
    }

    @Test
    void exchangeCurrency_missingParams_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/exchange-rate/exchange"))
                .andExpect(status().isBadRequest());
    }
}
