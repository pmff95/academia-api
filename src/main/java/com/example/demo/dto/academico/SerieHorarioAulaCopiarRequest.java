package com.example.demo.dto.academico;

import com.example.demo.domain.enums.Serie;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SerieHorarioAulaCopiarRequest(
        @Schema(description = "Série que receberá os horários", example = "SEGUNDO_ANO")
        @NotNull(message = "Série destino é obrigatória")
        Serie destino,

        @Schema(description = "Série utilizada como modelo", example = "PRIMEIRO_ANO")
        @NotNull(message = "Série origem é obrigatória")
        Serie origem
) {
}
