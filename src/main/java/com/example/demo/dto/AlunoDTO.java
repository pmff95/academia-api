package com.example.demo.dto;

import java.time.LocalDate;
import java.util.UUID;

public class AlunoDTO extends UsuarioDTO {
    private LocalDate dataMatricula;
    private UUID professorUuid;

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public UUID getProfessorUuid() {
        return professorUuid;
    }

    public void setProfessorUuid(UUID professorUuid) {
        this.professorUuid = professorUuid;
    }
}
