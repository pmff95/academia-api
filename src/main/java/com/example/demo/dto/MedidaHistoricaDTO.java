package com.example.demo.dto;

import com.example.demo.entity.Segmento;
import com.example.demo.entity.TipoMedida;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class MedidaHistoricaDTO {
    private Long id;
    private UUID uuid;
    private Long alunoId;
    private Segmento segmento;
    private TipoMedida tipoMedida;
    private BigDecimal valor;
    private String unidade;
    private ZonedDateTime dataMedicao;
    private String observacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Segmento getSegmento() {
        return segmento;
    }

    public void setSegmento(Segmento segmento) {
        this.segmento = segmento;
    }

    public TipoMedida getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(TipoMedida tipoMedida) {
        this.tipoMedida = tipoMedida;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public ZonedDateTime getDataMedicao() {
        return dataMedicao;
    }

    public void setDataMedicao(ZonedDateTime dataMedicao) {
        this.dataMedicao = dataMedicao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
