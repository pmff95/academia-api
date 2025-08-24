package com.example.demo.dto;

import com.example.demo.entity.Musculo;
import java.util.UUID;

public class ExercicioDTO {
    private UUID uuid;
    private String nome;
    private String descricao;
    private Musculo musculo;
    private UUID maquinaUuid;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Musculo getMusculo() {
        return musculo;
    }

    public void setMusculo(Musculo musculo) {
        this.musculo = musculo;
    }

    public UUID getMaquinaUuid() {
        return maquinaUuid;
    }

    public void setMaquinaUuid(UUID maquinaUuid) {
        this.maquinaUuid = maquinaUuid;
    }
}
