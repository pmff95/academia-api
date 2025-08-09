package com.example.demo.dto.projection;

import java.util.UUID;

public interface GrupoItinerarioSummary {
    UUID getUuid();

    String getNome();

    Integer getAnoLetivo();
}
