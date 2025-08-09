package com.example.demo.dto.projection;

import java.time.LocalDate;
import java.util.UUID;

public interface AtividadeSummary {
    UUID getUuid();

    String getTitulo();

    String getDescricao();

    LocalDate getPrazo();

    String getArquivoUrl();
}

