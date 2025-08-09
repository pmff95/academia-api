package com.example.demo.dto.academico;

import com.example.demo.domain.enums.DiaSemana;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record HorarioItinerarioRequest(
        @Schema(description = "Dia da semana")
        @NotNull(message = "Dia é obrigatório")
        DiaSemana dia,

        @Schema(description = "Ordem da aula", example = "1")
        @NotNull(message = "Ordem é obrigatória")
        Integer ordem
) {
}
