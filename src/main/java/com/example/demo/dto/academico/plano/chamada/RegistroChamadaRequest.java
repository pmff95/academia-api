package com.example.demo.dto.academico.plano.chamada;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RegistroChamadaRequest(
        @Schema(description = "UUID da do plano disciplina associado à chamada")
        @NotNull(message = "UUID Plano Disciplina é obrigatório")
        UUID planoDisciplinaUuid,

        @Schema(description = "Data da aula")
        @NotNull(message = "Data é obrigatória")
        LocalDate dataAula,

        @Schema(description = "Indica se a aula foi dupla")
        Boolean aulaDupla,

        @Schema(description = "Lista de presenças")
        @NotNull(message = "Presenças são obrigatórias")
        @Valid
        List<RegistroChamadaItemRequest> presencas
) {
}
