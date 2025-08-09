package com.example.demo.dto.professor;

import com.example.demo.domain.enums.DiaSemana;
import com.example.demo.domain.enums.Turno;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.UUID;

public record HorarioDisponivelRequest(

        @Schema(description = "UUID do horário")
        UUID uuid,

        @Schema(description = "Dia da semana", example = "SEGUNDA")
        DiaSemana dia,

        @Schema(description = "Hora de início", example = "08:00")
        LocalTime inicio,

        @Schema(description = "Hora de fim", example = "10:00")
        LocalTime fim,

        @Schema(description = "Turno do horário", example = "MANHA")
        Turno turno

) {
}
