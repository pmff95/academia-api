package com.example.demo.dto.professor;

import com.example.demo.dto.projection.ProfessorAulaHorarioItinerarioView;
import com.example.demo.dto.projection.ProfessorAulaHorarioView;

import java.util.List;

public record ProfessorAulasResponse(
        List<ProfessorAulaHorarioView> aulas,
        List<ProfessorAulaHorarioItinerarioView> itinerarios
) {
}
