package com.example.demo.dto.projection.escola;

import com.example.demo.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EscolaView {
    UUID getUuid();

    String getNome();

    String getNomeCurto();

    String getCnpj();

    Status getStatus();

    LocalDateTime getCriadoEm();
}
