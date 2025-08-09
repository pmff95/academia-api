package com.example.demo.dto.projection;

import java.util.UUID;

public interface TurmaDisciplinaIdAndName {

    UUID getUuid();

    UUID getDisciplinaUuid();

    String getDisciplinaNome();

    java.util.UUID getProfessorUuid();

    String getProfessorNome();

    Integer getCargaHoraria();

    Integer getAulasSemana();
}
