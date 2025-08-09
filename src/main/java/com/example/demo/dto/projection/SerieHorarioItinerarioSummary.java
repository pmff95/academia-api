package com.example.demo.dto.projection;

import com.example.demo.domain.enums.DiaSemana;

import java.time.LocalTime;
import java.util.UUID;

public interface SerieHorarioItinerarioSummary {
    UUID getUuid();

    DiaSemana getDia();

    Integer getOrdem();

    LocalTime getInicio();

    LocalTime getFim();
}
