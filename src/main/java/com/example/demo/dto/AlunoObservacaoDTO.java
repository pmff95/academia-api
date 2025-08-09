package com.example.demo.dto;

import com.example.demo.entity.ObservacaoTipo;

import java.time.LocalDateTime;

public class AlunoObservacaoDTO {
    private Long id;
    private String descricao;
    private ObservacaoTipo tipo;
    private LocalDateTime dataRegistro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
