package com.example.demo.dto.api.cobpag.normal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Calendario(
        @JsonProperty("expiracao") Integer expiracao,
        @JsonProperty("criacao") String criacao
) {
}
