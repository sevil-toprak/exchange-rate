package com.api.exchangerate.model.response;

public record PageData(
        long totalElements,
        int  totalPages,
        int  page,
        int  size
) {}
