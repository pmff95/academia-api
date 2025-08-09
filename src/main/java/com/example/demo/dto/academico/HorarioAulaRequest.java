package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioAulaRequest(
        @Schema(description = "Ordem da aula", example = "1")
        @NotNull(message = "Ordem da aula é obrigatória")
        Integer ordem,

        @Schema(description = "Hora de início")
        @NotNull(message = "Horário de início é obrigatório")
        LocalTime inicio,

        @Schema(description = "Hora de fim")
        @NotNull(message = "Horário de fim é obrigatório")
        LocalTime fim,

        @Schema(description = "Indica se este horário é intervalo", example = "false")
        Boolean intervalo
) {
}
