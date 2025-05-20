package com.api.exchangerate.service.spec;

import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.ExchangeApiConvertResponse;

public interface BuildRequestService {

    ExchangeApiConvertResponse buildExchangeCurrencyRequest(ExchangeRateRequest request);

    ExchangeApiConvertResponse buildConvertCurrencyRequest(ConvertRateRequest request);

}
