package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class AlunoPagamentoDTO {
    private UUID uuid;
    private LocalDate dataPagamento;
    private LocalDate dataVencimento;
    private BigDecimal valor;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
