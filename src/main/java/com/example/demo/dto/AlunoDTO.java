package com.example.demo.dto;

import java.time.LocalDate;
import java.util.UUID;

public class AlunoDTO extends UsuarioDTO {
    private LocalDate dataMatricula;
    private UUID professorUuid;
    private String codigoAcademia;

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

    public String getCodigoAcademia() {
        return codigoAcademia;
    }

    public void setCodigoAcademia(String codigoAcademia) {
        this.codigoAcademia = codigoAcademia;
    }
}
