package com.example.demo.dto.academico;

import com.example.demo.domain.enums.DiaSemana;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AulaHorarioVinculoRequest(
        @Schema(description = "UUID da turma/disciplina")
        @NotNull(message = "Disciplina é obrigatória")
        UUID disciplinaUuid,

        @Schema(description = "Dia da semana")
        @NotNull(message = "Dia é obrigatório")
        DiaSemana dia,

        @Schema(description = "Ordem da aula", example = "1")
        @NotNull(message = "Ordem é obrigatória")
        Integer ordem,

        @Schema(description = "UUID do grupo itinerário", required = false)
        UUID grupoItinerarioUuid
) {
}
