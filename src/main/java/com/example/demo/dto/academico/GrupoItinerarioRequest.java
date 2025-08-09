package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GrupoItinerarioRequest(
        @Schema(description = "Nome do grupo", example = "Grupo A")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 1, max = 100, message = "O nome deve ter entre 1 e 100 caracteres")
        String nome,

        @Schema(description = "Ano letivo", example = "2024")
        Integer anoLetivo
) {
}
