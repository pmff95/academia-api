package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class MercadoPagoPagamento {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    private String mercadoPagoId;

    private String status;

    private String tipo;

    private String detalhe;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
