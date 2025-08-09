package com.example.demo.dto.projection;

import com.example.demo.domain.enums.NivelEnsino;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public interface DisciplinaSummary {
    UUID getUuid();

    String getNome();

    Boolean getItinerario();

    Integer getCargaHoraria();

    /**
     * Valor original do enum persistido na entidade.
     */
    @JsonIgnore
    String getNivelEnsino();

    /**
     * Nome formatado do nível de ensino.
     */
    @JsonProperty("nivelEnsino")
    default String getNivelEnsinoFormatado() {
        return NivelEnsino.valueOf(getNivelEnsino()).getDescricao();
    }
}
