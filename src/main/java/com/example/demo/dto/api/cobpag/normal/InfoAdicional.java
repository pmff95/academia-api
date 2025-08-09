package com.example.demo.dto.api.cobpag.normal;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InfoAdicional(String nome, String valor) {
}
