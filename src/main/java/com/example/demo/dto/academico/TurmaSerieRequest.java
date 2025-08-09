package com.example.demo.dto.academico;

import com.example.demo.domain.enums.Serie;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TurmaSerieRequest(
        @Schema(description = "Série da turma", example = "PRIMEIRO_ANO")
        @NotNull(message = "A série é obrigatória")
        Serie serie
) {
}
