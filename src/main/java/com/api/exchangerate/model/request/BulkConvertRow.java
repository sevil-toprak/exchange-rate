package com.api.exchangerate.model.request;

import java.math.BigDecimal;

public record BulkConvertRow(
        String source,
        String target,
        BigDecimal amount
) {}
