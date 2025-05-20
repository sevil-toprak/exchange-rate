package com.api.exchangerate.service.spec;

import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;

public interface ExchangeRateService {
    ExchangeRateResponse exchangeCurrency(ExchangeRateRequest request);

    ConvertRateResponse convertCurrency(ConvertRateRequest request);

    BulkConvertRateResponse bulkConvertCurrency();
}
