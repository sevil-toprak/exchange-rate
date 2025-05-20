package com.api.exchangerate.service.impl;

import com.api.exchangerate.config.ExchangeApiProperties;
import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.constants.ErrorMessages;
import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.ExchangeApiConvertResponse;
import com.api.exchangerate.service.spec.BuildRequestService;
import com.api.exchangerate.util.HttpRequester;
import com.api.exchangerate.util.UrlMaskingUtil;
import com.api.exchangerate.util.spec.HttpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildRequestServiceImpl implements BuildRequestService {

    private final ExchangeApiProperties properties;
    private final HttpRequester httpRequester;

    private static final String API_CONVERT_PATH = "convert";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT = "amount";
    private static final String PARAM_KEY = "access_key";

    @Override
    public ExchangeApiConvertResponse buildExchangeCurrencyRequest(ExchangeRateRequest request) {
        String url = buildUrl(request.getSource().getCurrencyCode(), request.getTarget().getCurrencyCode(), BigDecimal.ONE);
        return callConvertEndpoint(url);
    }

    @Override
    public ExchangeApiConvertResponse buildConvertCurrencyRequest(ConvertRateRequest request) {
        String url = buildUrl(request.getSource().getCurrencyCode(), request.getTarget().getCurrencyCode(), request.getAmount());
        return callConvertEndpoint(url);
    }

    private String buildUrl(String from, String to, BigDecimal amount) {
        return UriComponentsBuilder.fromUriString(properties.getUrl())
                .path(API_CONVERT_PATH)
                .queryParam(PARAM_FROM, from)
                .queryParam(PARAM_TO, to)
                .queryParam(PARAM_AMOUNT, amount)
                .queryParam(PARAM_KEY, properties.getKey())
                .build()
                .toUriString();
    }

    private ExchangeApiConvertResponse callConvertEndpoint(String url) {
        String maskedUrl = UrlMaskingUtil.maskAccessKey(url);
        HttpRequest<MultiValueMap<String, String>, ExchangeApiConvertResponse> httpRequest =
                HttpRequest.<MultiValueMap<String, String>, ExchangeApiConvertResponse>builder()
                        .url(url)
                        .headers(defaultHeaders())
                        .responseType(ExchangeApiConvertResponse.class)
                        .build();

        try {
            log.info("External API request sent. URL: {}", maskedUrl);
            return httpRequester.get(httpRequest);
        } catch (Exception e) {
            log.error("External API integration failed during request to URL: {}", maskedUrl, e);
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR,
                    ErrorMessages.API_INTEGRATION_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, String> defaultHeaders() {
        return Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

}
