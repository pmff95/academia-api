package com.example.demo.dto.projection;

import com.example.demo.domain.enums.Serie;

import java.util.UUID;

/**
 * Projection used to fetch the series for each discipline taught by a professor.
 */
public interface DisciplinaSerieRow {
    UUID getDisciplinaUuid();

    String getDisciplinaNome();

    String getDisciplinaNivelEnsino();

    Serie getSerie();
}
