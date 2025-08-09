package com.example.demo.dto.academico.plano.avaliacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Requisição para criação de calendário de provas")
public record CalendarioProvaRequest(
        @Schema(description = "UUID da turma")
        @NotNull(message = "Informe a turma.")
        UUID turmaUuid,
        @Schema(description = "Dados do calendário de provas")
        @NotEmpty(message = "Informe ao menos um conjunto de dados.")
        List<ItemCalendarioRequest> dados
) {
    public record ItemCalendarioRequest(
            @Schema(description = "UUIDs das disciplinas")
            @NotEmpty(message = "Informe ao menos uma disciplina.")
            List<UUID> disciplinas,
            @Schema(description = "Datas das provas")
            @NotEmpty(message = "Informe ao menos uma data.")
            List<LocalDate> datas
    ) {
    }
}
