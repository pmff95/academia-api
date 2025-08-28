package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;


public class MercadoPagoQrCodeDTO {
    private String descricao;
    private BigDecimal valor;
    private String nomeContato;
    private String telefone;
    private String telefoneSecundario;

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
