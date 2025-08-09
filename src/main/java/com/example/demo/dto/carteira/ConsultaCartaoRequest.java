package com.example.demo.dto.carteira;

import io.swagger.v3.oas.annotations.media.Schema;

public record ConsultaCartaoRequest(
        @Schema(description = "Número do cartão")
        String numeroCartao,

        @Schema(description = "CPF do titular")
        String cpf,

        @Schema(description = "Nome do titular")
        String nome
) {
}
