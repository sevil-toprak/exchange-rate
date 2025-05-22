package com.api.exchangerate.controller;

import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import com.api.exchangerate.model.response.ExchangeRateHistoryResponse;
import com.api.exchangerate.service.business.ExchangeRateHistoryBusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rate/history")
@RequiredArgsConstructor
public class ExchangeRateHistoryController {

    private final ExchangeRateHistoryBusinessService historyService;

    @GetMapping
    public ResponseEntity<ExchangeRateHistoryResponse> filterExchangeRateHistory(@Valid ExchangeRateHistoryRequest request) {
        ExchangeRateHistoryResponse response = historyService.filterExchangeRateHistory(request);
        return ResponseEntity.ok(response);
    }
}
