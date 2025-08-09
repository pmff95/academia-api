package com.example.demo.dto.academico;

import com.example.demo.domain.enums.Serie;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SerieHorarioAulaCadastroRequest(
        @Schema(description = "Séries que utilizarão estes horários")
        @NotEmpty(message = "Informe ao menos uma série")
        List<Serie> series,

        @Schema(description = "Horários das aulas")
        @Valid
        @NotEmpty(message = "Informe ao menos um horário")
        List<HorarioAulaRequest> horarios
) {
}
