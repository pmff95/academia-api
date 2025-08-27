package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class MercadoPagoQrCodeDTO {
    private String descricao;
    private BigDecimal valor;

    private UUID enderecoUuid;

    private EnderecoDTO endereco;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public UUID getEnderecoUuid() {
        return enderecoUuid;
    }

    public void setEnderecoUuid(UUID enderecoUuid) {
        this.enderecoUuid = enderecoUuid;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }
}
