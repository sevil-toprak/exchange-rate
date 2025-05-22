package com.api.exchangerate.model.response;

import java.util.List;

public record BulkConvertRateResponse(
        List<ConvertRateResponse> rows,
        int totalCount,
        int successCount,
        int failedCount
) {
}