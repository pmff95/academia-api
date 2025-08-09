package com.example.demo.dto.academico;

import java.util.UUID;

/**
 * Dados básicos de uma disciplina com o nome do nível de ensino formatado.
 */
public record DisciplinaView(
        UUID uuid,
        String nome,
        String nivelEnsino,
        boolean itinerario,
        Integer cargaHoraria
) {
}
