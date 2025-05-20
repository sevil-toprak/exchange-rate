package com.api.exchangerate.exception.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErrorMessages {

    // Error Messages with Codes
    public static final String NOT_FOUND = "Not found.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error.";
    public static final String API_INTEGRATION_ERROR = "Api integration error.";
    public static final String CURRENCY_CODE_NOT_FOUND = "Currency code not found.";

    // Validation
    public static final String INVALID_REQUEST_MISSING_FILTERS = "At least one filter (transactionId or startDate + endDate) must be provided.";
    public static final String INVALID_REQUEST_MISSING_AMOUNT_PARAMETER = "Amount parameter should not be null.";
    public static final String INVALID_REQUEST_MISSING_CURRENCY_PARAMETER = "Currency parameter should not be null.";
    public static final String INVALID_PAGE_SIZE = "Limit must be between 1 and 100.";
    public static final String INVALID_PAGE_NUMBER = "Page must be 0 or greater.";
    public static final String INVALID_TRANSACTION_ID = "Invalid Transaction Id Format.";

}