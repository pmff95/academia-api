package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record SerieHorarioItinerarioCadastroRequest(
        @Schema(description = "Turmas que utilizarão estes horários")
        @NotEmpty(message = "Informe ao menos uma turma")
        List<UUID> turmas,

        @Schema(description = "Horários dos itinerários")
        @Valid
        @NotEmpty(message = "Informe ao menos um horário")
        List<HorarioItinerarioRequest> horarios
) {
}
