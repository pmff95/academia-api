package com.example.demo.dto.projection;

import java.util.UUID;

/**
 * Simplified view for DisciplinaGrupoItinerario exposing only the identifiers
 * and names of the linked disciplina.
 */
public interface DisciplinaGrupoItinerarioIdAndName {
    UUID getUuid();

    UUID getDisciplinaUuid();

    String getDisciplinaNome();

    Integer getCargaHoraria();

    Integer getAulasSemana();
}
