package com.example.demo.dto.professor;

import java.util.UUID;

/**
 * Discipline information including the series name where it is taught.
 */
public record DisciplinaSerieResponse(
        UUID uuid,
        String nome,
        String nivelEnsino,
        String serie
) {
}
