package com.example.demo.dto.academico;

import com.example.demo.domain.enums.Serie;

/**
 * Indica se uma série já possui horários cadastrados.
 */
public record SerieHorarioSerieView(
        Serie serie,
        boolean cadastrado
) {
}
