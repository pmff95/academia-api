package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Valor(
    String original,
    @JsonProperty("modalidadeAlteracao") Integer modalidadeAlteracao,
    Multa multa,
    Juros juros,
    Desconto desconto
) {}