package com.example.demo.dto;

import com.example.demo.domain.enums.TipoFornecedor;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoFiltroDTO {
    private String nome;
    private BigDecimal precoMin;
    private TipoFornecedor tipo;
    private BigDecimal precoMax;
    private List<String> tamanhos;
    private List<String> cores;
    private List<String> sabores;
    private List<String> volumes;
    private Boolean promocao;
    private List<String> marcas;
    private List<String> tipos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoMin() {
        return precoMin;
    }

    public void setPrecoMin(BigDecimal precoMin) {
        this.precoMin = precoMin;
    }

    public TipoFornecedor getTipo() {
        return tipo;
    }

    public void setTipo(TipoFornecedor tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecoMax() {
        return precoMax;
    }

    public void setPrecoMax(BigDecimal precoMax) {
        this.precoMax = precoMax;
    }

    public List<String> getTamanhos() {
        return tamanhos;
    }

    public void setTamanhos(List<String> tamanhos) {
        this.tamanhos = tamanhos;
    }

    public List<String> getCores() {
        return cores;
    }

    public void setCores(List<String> cores) {
        this.cores = cores;
    }

    public List<String> getSabores() {
        return sabores;
    }

    public void setSabores(List<String> sabores) {
        this.sabores = sabores;
    }

    public List<String> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<String> volumes) {
        this.volumes = volumes;
    }

    public Boolean getPromocao() {
        return promocao;
    }

    public void setPromocao(Boolean promocao) {
        this.promocao = promocao;
    }

    public List<String> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }
}

