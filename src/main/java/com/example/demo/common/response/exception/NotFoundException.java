package com.example.demo.common.response.exception;

import com.example.demo.common.response.ErrorType;

public class NotFoundException extends EurekaException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message, Class<?> clazz) {
        super(ErrorType.NOT_FOUND, message, clazz);
    }

    public NotFoundException(String message, Throwable cause, Class<?> clazz) {
        super(ErrorType.NOT_FOUND, ErrorType.NOT_FOUND.getCode(), message, cause, clazz);
    }
}
