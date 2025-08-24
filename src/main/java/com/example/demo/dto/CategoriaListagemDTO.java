package com.example.demo.dto;

import com.example.demo.entity.Musculo;

import java.util.List;
import java.util.UUID;

public class CategoriaListagemDTO {
    private UUID uuid;
    private String nome;
    private List<Musculo> musculos;

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

    public List<Musculo> getMusculos() {
        return musculos;
    }

    public void setMusculos(List<Musculo> musculos) {
        this.musculos = musculos;
    }
}
