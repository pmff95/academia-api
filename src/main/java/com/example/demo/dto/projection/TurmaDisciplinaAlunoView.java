package com.example.demo.dto.projection;

import java.util.UUID;

/**
 * Projeção para listar disciplinas do aluno e, quando houver, informações do grupo de itinerário.
 */
public interface TurmaDisciplinaAlunoView {
    UUID getTurmaUuid();

    UUID getUuid();

    UUID getDisciplinaUuid();

    String getDisciplinaNome();

    UUID getProfessorUuid();

    String getProfessorNome();

    Integer getCargaHoraria();

    UUID getGrupoUuid();

    String getGrupoNome();

    Boolean getItinerario();
}
