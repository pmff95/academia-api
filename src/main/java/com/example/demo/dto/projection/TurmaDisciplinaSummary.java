package com.example.demo.dto.projection;

import java.util.UUID;

public interface TurmaDisciplinaSummary {
    UUID getUuid();

    DisciplinaSummary getDisciplina();

    ProfessorSummary getProfessor();

    Integer getCargaHoraria();
}
