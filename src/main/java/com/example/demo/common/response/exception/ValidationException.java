package com.example.demo.common.response.exception;

import com.example.demo.common.response.ErrorType;

public class ValidationException extends EurekaException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message, Class<?> clazz) {
        super(ErrorType.VALIDATION, message, clazz);
    }

    public ValidationException(String message, Throwable cause, Class<?> clazz) {
        super(ErrorType.VALIDATION, ErrorType.VALIDATION.getCode(), message, cause, clazz);
    }
}

