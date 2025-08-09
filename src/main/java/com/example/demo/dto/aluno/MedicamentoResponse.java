package com.example.demo.dto.aluno;

import java.util.UUID;

public record MedicamentoResponse(
        UUID uuid,
        String nome,
        String dosagem,
        String frequencia,
        String observacoes
) {
}
