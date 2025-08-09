package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParametrosConsulta(
    String inicio,
    String fim,
    Paginacao paginacao
) {}