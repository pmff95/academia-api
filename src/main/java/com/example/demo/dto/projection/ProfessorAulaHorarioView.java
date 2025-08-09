package com.example.demo.dto.projection;

import com.example.demo.domain.enums.DiaSemana;

import java.time.LocalTime;
import java.util.UUID;

public interface ProfessorAulaHorarioView {
    UUID getTurmaUuid();

    String getTurmaNome();

    UUID getDisciplinaUuid();

    String getDisciplinaNome();

    Integer getOrdem();

    DiaSemana getDia();

    LocalTime getInicio();

    LocalTime getFim();
}
