package com.api.exchangerate.util.spec;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpRequest<T, R> {
    private String url;
    private Map<String, String> headers;
    private T body;
    private Class<R> responseType;

}