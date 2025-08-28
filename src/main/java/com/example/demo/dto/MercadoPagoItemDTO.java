package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class MercadoPagoItemDTO {
    private UUID produtoUuid;
    private String titulo;
    private Integer quantidade;
    private BigDecimal valor;

    public UUID getProdutoUuid() {
        return produtoUuid;
    }

    public void setProdutoUuid(UUID produtoUuid) {
        this.produtoUuid = produtoUuid;
    }

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
}
