package com.example.demo.dto.academico;

import com.example.demo.dto.projection.SerieHorarioAulaSummary;
import com.example.demo.dto.projection.SerieHorarioItinerarioSummary;

import java.util.List;

/**
 * Horários padrão da série e reservas de itinerário para uma turma.
 */
public record SerieHorarioAulaTurmaView(
        List<SerieHorarioAulaSummary> horarios,
        List<SerieHorarioItinerarioSummary> itinerarios
) {
}
