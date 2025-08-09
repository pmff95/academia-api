package com.example.demo.dto;

import java.util.List;

public class FichaTreinoDTO {
    private Long id;
    private Long alunoId;
    private Long professorId;
    private List<Long> exerciciosIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public List<Long> getExerciciosIds() {
        return exerciciosIds;
    }

    public void setExerciciosIds(List<Long> exerciciosIds) {
        this.exerciciosIds = exerciciosIds;
    }
}
