package com.api.exchangerate.util.impl;

import com.api.exchangerate.util.HttpRequester;
import com.api.exchangerate.util.spec.HttpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestClientRequester implements HttpRequester {

    private final RestClient restClient;

    @Override
    public <T, R> R get(HttpRequest<T, R> request) {
        return restClient.get()
                .uri(request.getUrl())
                .headers(headers -> headers.setAll(request.getHeaders()))
                .retrieve()
                .body(request.getResponseType());
    }

    @Override
    public <T, R> R post(HttpRequest<T, R> request) {
        return restClient.post()
                .uri(request.getUrl())
                .headers(headers -> headers.setAll(request.getHeaders()))
                .body(request.getBody())
                .retrieve()
                .body(request.getResponseType());
    }
}
