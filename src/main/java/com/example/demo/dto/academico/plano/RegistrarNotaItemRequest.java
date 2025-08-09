package com.example.demo.dto.academico.plano;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrarNotaItemRequest(
        @Schema(description = "UUID do aluno")
        @NotNull(message = "Aluno é obrigatório")
        UUID alunoUuid,

        @Schema(description = "Nota da entrega")
        @NotNull(message = "Nota é obrigatória")
        Double nota
) {
}
