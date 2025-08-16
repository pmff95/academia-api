package com.example.demo.dto;

import com.example.demo.domain.enums.TipoNotificacao;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificacaoDTO {
    private UUID uuid;
    private String mensagem;
    private TipoNotificacao tipo;
    private boolean lida;
    private LocalDateTime dataCriacao;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
