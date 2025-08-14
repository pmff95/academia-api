package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public class FichaTreinoDTO {
    private UUID uuid;
    private UUID alunoUuid;
    private UUID professorUuid;
    private String categoria;
    private boolean preset;
    private List<UUID> exerciciosUuids;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }

    public List<UUID> getExerciciosUuids() {
        return exerciciosUuids;
    }

    public void setExerciciosUuids(List<UUID> exerciciosUuids) {
        this.exerciciosUuids = exerciciosUuids;
    }
}
