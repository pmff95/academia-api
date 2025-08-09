package com.example.demo.common.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.response.ErrorType;
import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.response.exception.NoContentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.exception.ConstraintViolationException;


@RestControllerAdvice
public class EurekaExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(EurekaExceptionHandler.class);

    @ExceptionHandler(EurekaException.class)
    public ResponseEntity<ApiReturn<?>> handleEurekaException(EurekaException ex) {
        ApiReturn<?> apiReturn = ApiReturn.ofEurekaException(ex);
        HttpStatus status = HttpStatus.valueOf(ex.getErrorCode());

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(apiReturn, status);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ApiReturn<?>> handleNoContentException(NoContentException ex) {
        ApiReturn<?> apiReturn = ApiReturn.ofNoContentException(ex);
        HttpStatus status = HttpStatus.valueOf(ex.getErrorCode());

        log.info(ex.getMessage(), ex);

        return new ResponseEntity<>(apiReturn, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiReturn<?>> handleValidationException(MethodArgumentNotValidException ex) {
        return handleEurekaException(EurekaException.ofValidation(ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiReturn<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        String message = "Violação de integridade de dados";
        if (cause instanceof ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();
            if (constraint != null) {
                switch (constraint) {
                    case "uk_usuario_escola_cpf", "usuario_cpf_key" ->
                            message = "CPF já cadastrado para esta escola";
                    case "uk_usuario_escola_email", "usuario_email_key" ->
                            message = "Email já cadastrado para esta escola";
                    case "usuario_telefone_key", "idx_usuario_telefone" ->
                            message = "Telefone já cadastrado";
                }
            }
        }
        ApiReturn<?> apiReturn = ApiReturn.of(ErrorType.VALIDATION, ErrorType.VALIDATION.getCode(), message, ex);
        return new ResponseEntity<>(apiReturn, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiReturn<?>> handleGeneralException(Exception ex) {
        ApiReturn<?> apiReturn = ApiReturn.ofException(ex);
        return new ResponseEntity<>(apiReturn, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

