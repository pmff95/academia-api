package com.example.demo.dto.professor;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record HorarioDisponivelCadastroRequest(

        @Schema(description = "Dados dos horários disponíveis")
        List<HorarioDisponivelRequest> horarios,

        @Schema(description = "UUID do professor", example = "00000000-0000-0000-0000-000000000000")
        UUID uuid

) {
}
