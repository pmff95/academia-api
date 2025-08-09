package com.example.demo.common.validation.annotation.impl;

import com.example.demo.common.validation.annotation.Chave;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChaveValidator implements ConstraintValidator<Chave, String> {

    private static final String PATTERN = "^[a-z0-9_]+$";

    @Override
    public void initialize(Chave constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return value.matches(PATTERN);
    }
}
