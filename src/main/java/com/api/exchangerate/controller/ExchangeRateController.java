package com.api.exchangerate.controller;

import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;
import com.api.exchangerate.service.business.ExchangeRateBusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateBusinessService businessService;

    @GetMapping("/exchange")
    public ResponseEntity<ExchangeRateResponse> exchangeCurrency(@Valid ExchangeRateRequest request) {
        ExchangeRateResponse response = businessService.exchangeCurrency(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/convert")
    public ResponseEntity<ConvertRateResponse> convertCurrency(@Valid ConvertRateRequest request) {
        ConvertRateResponse response = businessService.convertCurrency(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk-convert")
    public ResponseEntity<BulkConvertRateResponse> bulkConvertCurrency(@RequestParam("file") MultipartFile file) {
            BulkConvertRateResponse response = businessService.bulkConvertCurrency(file);
        return ResponseEntity.ok(response);
    }

}
