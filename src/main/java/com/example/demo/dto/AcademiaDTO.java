package com.example.demo.dto;

public class AcademiaDTO {
    private Long id;
    private String nome;
    private UsuarioDTO admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UsuarioDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UsuarioDTO admin) {
        this.admin = admin;
    }
}
