package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AlunoMedidaDTO {
    private UUID uuid;
    private BigDecimal peso;
    private BigDecimal altura;
    private BigDecimal bracoEsquerdo;
    private BigDecimal bracoDireito;
    private BigDecimal peito;
    private BigDecimal abdomen;
    private BigDecimal cintura;
    private BigDecimal quadril;
    private BigDecimal coxaEsquerda;
    private BigDecimal coxaDireita;
    private BigDecimal panturrilhaEsquerda;
    private BigDecimal panturrilhaDireita;
    private LocalDateTime dataRegistro;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public BigDecimal getBracoEsquerdo() {
        return bracoEsquerdo;
    }

    public void setBracoEsquerdo(BigDecimal bracoEsquerdo) {
        this.bracoEsquerdo = bracoEsquerdo;
    }

    public BigDecimal getBracoDireito() {
        return bracoDireito;
    }

    public void setBracoDireito(BigDecimal bracoDireito) {
        this.bracoDireito = bracoDireito;
    }

    public BigDecimal getPeito() {
        return peito;
    }

    public void setPeito(BigDecimal peito) {
        this.peito = peito;
    }

    public BigDecimal getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(BigDecimal abdomen) {
        this.abdomen = abdomen;
    }

    public BigDecimal getCintura() {
        return cintura;
    }

    public void setCintura(BigDecimal cintura) {
        this.cintura = cintura;
    }

    public BigDecimal getQuadril() {
        return quadril;
    }

    public void setQuadril(BigDecimal quadril) {
        this.quadril = quadril;
    }

    public BigDecimal getCoxaEsquerda() {
        return coxaEsquerda;
    }

    public void setCoxaEsquerda(BigDecimal coxaEsquerda) {
        this.coxaEsquerda = coxaEsquerda;
    }

    public BigDecimal getCoxaDireita() {
        return coxaDireita;
    }

    public void setCoxaDireita(BigDecimal coxaDireita) {
        this.coxaDireita = coxaDireita;
    }

    public BigDecimal getPanturrilhaEsquerda() {
        return panturrilhaEsquerda;
    }

    public void setPanturrilhaEsquerda(BigDecimal panturrilhaEsquerda) {
        this.panturrilhaEsquerda = panturrilhaEsquerda;
    }

    public BigDecimal getPanturrilhaDireita() {
        return panturrilhaDireita;
    }

    public void setPanturrilhaDireita(BigDecimal panturrilhaDireita) {
        this.panturrilhaDireita = panturrilhaDireita;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
