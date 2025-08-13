package com.example.demo.exception;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.response.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiReturn<String>> handleApiException(ApiException ex) {
        ApiReturn<String> response = ApiReturn.of(ErrorType.VALIDATION, ErrorType.VALIDATION.getCode(), ex.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }
}
