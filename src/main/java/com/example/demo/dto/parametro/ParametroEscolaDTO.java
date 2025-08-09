package com.example.demo.dto.parametro;

import com.example.demo.common.validation.annotation.Chave;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ParametroEscolaDTO(
        @Schema(description = "Chave do parâmetro", example = "limite_maximo")
        @NotBlank(message = "Chave do parâmetro é obrigatória")
        @Chave
        String chaveParametro,

        @Schema(description = "Valor customizado para a escola", example = "200")
        String valorCustomizado
) {
}
