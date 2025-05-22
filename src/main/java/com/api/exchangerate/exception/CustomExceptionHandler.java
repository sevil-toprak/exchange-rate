package com.api.exchangerate.exception;

import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<ErrorResponse> handleApiException(BaseException e) {
        ErrorResponse response = new ErrorResponse(e.getHttpStatus(), e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        if (!allErrors.isEmpty()) {
            String defaultMessage = allErrors.getFirst().getDefaultMessage();
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, defaultMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}