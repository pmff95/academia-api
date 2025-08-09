package com.example.demo.dto.projection;

import java.util.List;
import java.util.UUID;


/**
 * Projection for ItinerarioFormativo returning basic data and groups.
 */

public interface ItinerarioFormativoSummary {
    UUID getUuid();

    String getNome();

    String getDescricao();

    /**
     * Groups associated with the itinerary containing only uuid and name.
     */
    List<GrupoItinerarioIdAndName> getGrupos();
}
