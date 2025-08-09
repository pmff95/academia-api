package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.GrauAlergia;
import com.example.demo.domain.enums.TipoAlergia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AlergiaRequest(
        @Schema(description = "Tipo da alergia", example = "ALIMENTAR")
        TipoAlergia tipo,

        @Schema(description = "Substância ou item alérgeno", example = "Amendoim")
        String substancia,

        @Schema(description = "Grau de gravidade", example = "GRAVE")
        GrauAlergia gravidade,

        @Schema(description = "Observações adicionais")
        String observacoes,

        @Schema(description = "Data de diagnóstico")
        LocalDate dataDiagnostico,

        @Schema(description = "Requer cuidados emergenciais?")
        Boolean cuidadosEmergenciais
) {
}
