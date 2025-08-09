package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AlunoGrupoItinerarioRequest(
        @Schema(description = "UUID do aluno")
        @NotNull(message = "Aluno é obrigatório")
        UUID alunoUuid
) {
}
