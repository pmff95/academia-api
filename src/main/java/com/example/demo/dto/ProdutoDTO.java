package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class ProdutoDTO {
    private UUID uuid;
    private UUID fornecedorUuid;
    private String nome;
    private String descricao;
    @NotBlank
    private String tipo;
    private String marca;
    private List<ProdutoDetalheDTO> detalhe;
    private Boolean ativo;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getFornecedorUuid() {
        return fornecedorUuid;
    }

    public void setFornecedorUuid(UUID fornecedorUuid) {
        this.fornecedorUuid = fornecedorUuid;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public List<ProdutoDetalheDTO> getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(List<ProdutoDetalheDTO> detalhe) {
        this.detalhe = detalhe;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}

