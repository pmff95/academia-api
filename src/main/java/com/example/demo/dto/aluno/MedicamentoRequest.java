package com.example.demo.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;

public record MedicamentoRequest(
        @Schema(description = "Nome do medicamento", example = "Dipirona")
        String nome,

        @Schema(description = "Dosagem do medicamento", example = "500mg")
        String dosagem,

        @Schema(description = "Frequência de uso", example = "2 vezes ao dia")
        String frequencia,

        @Schema(description = "Observações adicionais")
        String observacoes
) {
}
