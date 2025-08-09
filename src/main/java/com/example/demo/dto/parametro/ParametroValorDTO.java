package com.example.demo.dto.parametro;

import io.swagger.v3.oas.annotations.media.Schema;

public record ParametroValorDTO(
        @Schema(description = "Chave do parâmetro", example = "limite_maximo")
        String chave,

        @Schema(description = "Descrição do parâmetro")
        String descricao,

        @Schema(description = "Valor efetivo do parâmetro")
        String valor,

        @Schema(description = "Indica se o valor foi sobrescrito pela escola", example = "true")
        Boolean sobrescrito
) {
}
