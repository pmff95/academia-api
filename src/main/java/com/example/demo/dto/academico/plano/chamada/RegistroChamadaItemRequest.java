package com.example.demo.dto.academico.plano.chamada;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistroChamadaItemRequest(
        @Schema(description = "UUID do aluno")
        @NotNull(message = "Aluno é obrigatório")
        UUID alunoUuid,

        @Schema(description = "Indica se o aluno estava presente")
        @NotNull(message = "Presença é obrigatória")
        Boolean presente
) {
}
