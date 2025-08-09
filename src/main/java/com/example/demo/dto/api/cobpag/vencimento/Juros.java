package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Juros(
    Integer modalidade,
    @JsonProperty("valorPerc") String valorPerc
) {}