package com.example.demo.dto;

import java.util.UUID;

public class ExercicioDTO {
    private UUID uuid;
    private String nome;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
