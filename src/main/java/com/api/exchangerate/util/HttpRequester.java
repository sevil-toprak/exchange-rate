package com.api.exchangerate.util;

import com.api.exchangerate.util.spec.HttpRequest;

public interface HttpRequester {
    // T: RequestBodyType
    // R: ResponseType

    <T, R> R get(HttpRequest<T, R> requestDto);

    <T, R> R post(HttpRequest<T, R> requestDto);
}