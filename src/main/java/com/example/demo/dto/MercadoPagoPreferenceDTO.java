package com.example.demo.dto;

import java.math.BigDecimal;

import com.example.demo.dto.EntregaDTO;

public class MercadoPagoPreferenceDTO {
    private String titulo;
    private Integer quantidade;
    private BigDecimal valor;
    private String notificationUrl;
    private EntregaDTO entrega;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public EntregaDTO getEntrega() {
        return entrega;
    }

    public void setEntrega(EntregaDTO entrega) {
        this.entrega = entrega;
    }
}

