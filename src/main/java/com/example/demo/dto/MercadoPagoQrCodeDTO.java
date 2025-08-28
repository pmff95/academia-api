package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MercadoPagoQrCodeDTO {
    private String descricao;
    private BigDecimal valor;

    private UUID enderecoUuid;

    private EnderecoDTO endereco;

    private List<MercadoPagoItemDTO> itens;

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

    public List<MercadoPagoItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<MercadoPagoItemDTO> itens) {
        this.itens = itens;
    }
}
