package com.example.demo.dto.projection;

import com.example.demo.domain.enums.DiaSemana;

public interface TurmaAulaHorarioView {
    java.util.UUID getDisciplinaUuid();

    Integer getOrdem();

    String getDisciplinaNome();

    String getProfessorNome();

    DiaSemana getDia();
}
