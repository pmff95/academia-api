package com.example.demo.dto;

import com.example.demo.entity.ObservacaoTipo;

import java.time.LocalDateTime;
import java.util.UUID;

public class AlunoObservacaoDTO {
    private UUID uuid;
    private String descricao;
    private ObservacaoTipo tipo;
    private LocalDateTime dataRegistro;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ObservacaoTipo getTipo() {
        return tipo;
    }

    public void setTipo(ObservacaoTipo tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
