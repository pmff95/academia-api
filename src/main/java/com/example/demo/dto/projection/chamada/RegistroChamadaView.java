package com.example.demo.dto.projection.chamada;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Projeção de um registro de chamada com indicador se já foi registrado na data.
 */
public interface RegistroChamadaView {
    UUID getUuid();

    LocalDate getDataAula();

    Boolean getAulaDupla();

    List<ItemChamadaView> getPresencas();

    Boolean getRegistrado();
}
