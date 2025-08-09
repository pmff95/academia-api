package com.example.demo.dto.projection;

import java.util.UUID;

public interface DisciplinaGrupoItinerarioSummary {
    UUID getUuid();

    DisciplinaSummary getDisciplina();

    ProfessorSummary getProfessor();

    Integer getCargaHoraria();

    Integer getAulasSemana();
}
