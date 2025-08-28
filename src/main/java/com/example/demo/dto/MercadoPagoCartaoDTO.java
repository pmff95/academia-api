package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class MercadoPagoCartaoDTO {
    private String token;
    private BigDecimal valor;
    private String descricao;
    private Integer parcelas;
    private String metodo;
    private String email;
    private String docType;
    private String docNumber;
    private String nomeContato;
    private String telefone;
    private String telefoneSecundario;

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

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public void setTelefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
    }

    public List<MercadoPagoItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<MercadoPagoItemDTO> itens) {
        this.itens = itens;
    }

}
