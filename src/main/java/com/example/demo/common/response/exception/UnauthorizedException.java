package com.example.demo.common.response.exception;

import com.example.demo.common.response.ErrorType;

public class UnauthorizedException extends EurekaException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message, Class<?> clazz) {
        super(ErrorType.UNAUTHORIZED, message, clazz);
    }

    public UnauthorizedException(String message, Throwable cause, Class<?> clazz) {
        super(ErrorType.UNAUTHORIZED, ErrorType.UNAUTHORIZED.getCode(), message, cause, clazz);
    }
}
