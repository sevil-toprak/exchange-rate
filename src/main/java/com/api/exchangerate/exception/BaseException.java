package com.api.exchangerate.exception;

import com.api.exchangerate.exception.constants.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode code;
    private final String message;
    private final HttpStatus httpStatus;

    public BaseException(ErrorCode code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
