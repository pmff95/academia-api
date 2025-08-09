package com.example.demo.dto.projection;

import java.util.UUID;

/**
 * Simplified view of GrupoItinerario containing only identifier and name.
 */
public interface GrupoItinerarioIdAndName {
    UUID getUuid();

    String getNome();
}
