package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Recebedor(
    String nome,
    @JsonProperty("cpf") String cpf,
    @JsonProperty("cnpj") String cnpj,
    String logradouro,
    String cidade,
    String uf,
    String cep
) {}