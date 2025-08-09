package com.example.demo.common.validation.annotation;

import com.example.demo.common.validation.annotation.impl.ChaveValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChaveValidator.class)
@Documented
public @interface Chave {
    String message() default "Chave inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
