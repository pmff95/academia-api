package com.example.demo.dto.academico.plano;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegistrarNotaRequest(
        @Schema(description = "Lista de notas dos alunos")
        @NotNull(message = "Notas são obrigatórias")
        @Valid
        List<RegistrarNotaItemRequest> notas
) {
}
