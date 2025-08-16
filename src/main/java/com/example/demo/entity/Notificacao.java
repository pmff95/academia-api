package com.example.demo.entity;

import com.example.demo.domain.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Notificacao {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destinatario_uuid")
    private Usuario destinatario;

    @Column(nullable = false)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(nullable = false)
    private boolean lida = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
