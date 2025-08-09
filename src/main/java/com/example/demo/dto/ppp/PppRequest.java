package com.example.demo.dto.ppp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PppRequest(
        @Schema(description = "UUID da escola")
        @NotNull UUID escolaUuid,

        @Schema(description = "Data de início da vigência", example = "2024-01-01")
        @NotNull LocalDate inicioVigencia,

        @Schema(description = "Data de término da vigência", example = "2027-12-31")
        @NotNull LocalDate fimVigencia,

        @Schema(description = "Data de aprovação do PPP")
        LocalDate dataAprovacao,

        @Schema(description = "Responsável técnico")
        String responsavel,

        @Schema(description = "Conteúdo em HTML do PPP")
        @NotNull String conteudo
) {
}
