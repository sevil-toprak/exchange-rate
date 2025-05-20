package com.api.exchangerate.model.request;

import com.api.exchangerate.exception.constants.ErrorMessages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateHistoryRequest extends PageRequest {

    @UUID(version = 4, message = ErrorMessages.INVALID_TRANSACTION_ID)
    private String transactionId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime endDate;

    @AssertTrue(message = ErrorMessages.INVALID_REQUEST_MISSING_FILTERS)
    @JsonIgnore
    public boolean isValidRequest() {
        return Objects.nonNull(transactionId) || (Objects.nonNull(startDate) && Objects.nonNull(endDate));
    }
}
