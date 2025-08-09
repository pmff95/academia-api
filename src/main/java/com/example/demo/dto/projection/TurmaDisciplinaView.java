package com.example.demo.dto.projection;

import java.util.UUID;

public interface TurmaDisciplinaView {
    UUID getDisciplinaUuid();

    UUID getTurmaUuid();

    String getNomeTurma();

    String getNomeDisciplina();

    String getNivelEnsino();

    String getSerie();
    UUID getPlanoDisciplinaUuid();
}
