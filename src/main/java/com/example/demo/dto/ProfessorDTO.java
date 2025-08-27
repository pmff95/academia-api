package com.example.demo.dto;

import java.time.LocalDate;

public class ProfessorDTO extends UsuarioDTO {
    private String cref;
    private LocalDate crefValidade;

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }

    public LocalDate getCrefValidade() {
        return crefValidade;
    }

    public void setCrefValidade(LocalDate crefValidade) {
        this.crefValidade = crefValidade;
    }
}
