package com.example.demo.dto.academico;

import com.example.demo.domain.enums.DiaSemana;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record AulaHorarioRequest(
        @Schema(description = "Dia da semana")
        @NotNull(message = "Dia é obrigatório")
        DiaSemana dia,

        @Schema(description = "Hora de início")
        @NotNull(message = "Horário de início é obrigatório")
        LocalTime inicio,

        @Schema(description = "Hora de fim")
        @NotNull(message = "Horário de fim é obrigatório")
        LocalTime fim
) {
}
