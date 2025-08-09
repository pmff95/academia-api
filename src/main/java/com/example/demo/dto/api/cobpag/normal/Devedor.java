package com.example.demo.dto.api.cobpag.normal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Devedor(
        String nome,
        @JsonProperty("cpf") String cpf,
        @JsonProperty("cnpj") String cnpj
) {
}
