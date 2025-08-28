package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MercadoPagoCartaoDTO {
    private String token;
    private BigDecimal valor;
    private String descricao;
    private Integer parcelas;
    private String metodo;
    private String email;
    private String docType;
    private String docNumber;

    private UUID enderecoUuid;

    private EnderecoDTO endereco;

    private List<MercadoPagoItemDTO> itens;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
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
