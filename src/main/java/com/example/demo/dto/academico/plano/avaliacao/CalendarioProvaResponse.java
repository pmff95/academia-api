package com.example.demo.dto.academico.plano.avaliacao;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Resposta do calendário de provas")
public record CalendarioProvaResponse(
        @Schema(description = "UUID da turma")
        UUID turmaUuid,
        @Schema(description = "Lista de avaliações")
        List<ItemCalendarioResponse> avaliacoes
) {
    public record ItemCalendarioResponse(
            @Schema(description = "UUID da disciplina")
            UUID disciplinaUuid,
            @Schema(description = "Data da prova")
            LocalDate data,
            @Schema(description = "Título da avaliação")
            String titulo
    ) {
    }
}
