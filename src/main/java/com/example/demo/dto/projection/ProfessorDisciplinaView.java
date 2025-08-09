package com.example.demo.dto.projection;

import java.util.List;
import java.util.UUID;

/**
 * Projection for listing professors with their disciplines.
 */
public interface ProfessorDisciplinaView {
    UUID getUuid();

    String getNome();

    List<DisciplinaSummary> getDisciplinas();
}
