package com.api.exchangerate.model.request;

import com.api.exchangerate.exception.constants.ErrorMessages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    @Builder.Default
    @Min(value = 1, message = ErrorMessages.INVALID_PAGE_SIZE)
    @Max(value = 100, message = ErrorMessages.INVALID_PAGE_SIZE)
    private int limit = 20;

    @Builder.Default
    @Min(value = 0, message = ErrorMessages.INVALID_PAGE_NUMBER)
    private int page = 0;
}
