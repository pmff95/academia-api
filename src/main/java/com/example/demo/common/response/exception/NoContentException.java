package com.example.demo.common.response.exception;

import com.example.demo.common.response.ErrorType;

public class NoContentException extends EurekaException {
    private static final long serialVersionUID = 1L;

    public NoContentException(String message, Class<?> clazz) {
        super(ErrorType.NO_CONTENT, message, clazz);
    }

    public NoContentException(String message, Throwable cause, Class<?> clazz) {
        super(ErrorType.NO_CONTENT, ErrorType.NO_CONTENT.getCode(), message, cause, clazz);
    }
}
