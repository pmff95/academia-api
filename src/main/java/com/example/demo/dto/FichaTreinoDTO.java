package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public class FichaTreinoDTO {
    private Long id;
    private UUID alunoUuid;
    private UUID professorUuid;
    private List<Long> exerciciosIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getAlunoUuid() {
        return alunoUuid;
    }

    public void setAlunoUuid(UUID alunoUuid) {
        this.alunoUuid = alunoUuid;
    }

    public UUID getProfessorUuid() {
        return professorUuid;
    }

    public void setProfessorUuid(UUID professorUuid) {
        this.professorUuid = professorUuid;
    }

    public List<Long> getExerciciosIds() {
        return exerciciosIds;
    }

    public void setExerciciosIds(List<Long> exerciciosIds) {
        this.exerciciosIds = exerciciosIds;
    }
}
