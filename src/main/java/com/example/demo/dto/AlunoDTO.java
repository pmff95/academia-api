package com.example.demo.dto;

import com.example.demo.entity.StatusAluno;

import java.time.LocalDate;

public class AlunoDTO extends UsuarioDTO {
    private LocalDate dataMatricula;
    private StatusAluno status;
    private Long professorId;

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

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }
}
