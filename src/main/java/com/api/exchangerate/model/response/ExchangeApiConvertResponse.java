package com.api.exchangerate.model.response;

public record ExchangeApiConvertResponse(
        boolean success,
        String terms,
        String privacy,
        Query query,
        Info info,
        double result
) {
    public record Query(
            String from,
            String to,
            double amount
    ) {
    }

    public record Info(
            long timestamp,
            double quote
    ) {
    }
}
