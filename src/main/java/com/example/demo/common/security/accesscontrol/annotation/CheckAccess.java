package com.example.demo.common.security.accesscontrol.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CheckAccess {

    /**
     * Nome da entidade para a qual a política de acesso será aplicada (ex.: "escola").
     */
    String entity();

    /**
     * Nome do parâmetro do método que contém o identificador do recurso (default: "uuid").
     */
    String paramName() default "uuid";
}
