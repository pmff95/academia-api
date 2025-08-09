package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TurmaDisciplinaRequest(
        @Schema(description = "UUID do vínculo")
        UUID uuid,

        @Schema(description = "UUID do professor")
        @NotNull(message = "Professor é obrigatório")
        UUID professorUuid,

        @Schema(description = "UUID da disciplina")
        @NotNull(message = "Disciplina é obrigatória")
        UUID disciplinaUuid,

        @Schema(description = "Aulas por semana")
        @NotNull(message = "Aulas por semana são obrigatórias")
        Integer aulasSemana
) {
}
