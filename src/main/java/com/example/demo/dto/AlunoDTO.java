package com.example.demo.dto;

import com.example.demo.entity.StatusAluno;

import java.time.LocalDate;
import java.util.UUID;

public class AlunoDTO extends UsuarioDTO {
    private LocalDate dataMatricula;
    private StatusAluno status;
    private UUID professorUuid;

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public StatusAluno getStatus() {
        return status;
    }

    public void setStatus(StatusAluno status) {
        this.status = status;
    }

    public UUID getProfessorUuid() {
        return professorUuid;
    }

    public void setProfessorUuid(UUID professorUuid) {
        this.professorUuid = professorUuid;
    }
}
