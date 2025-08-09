package com.example.demo.dto.projection;

import com.example.demo.domain.enums.DiaSemana;

import java.time.LocalTime;
import java.util.UUID;

public interface AulaHorarioSummary {
    UUID getUuid();

    DiaSemana getDia();

    LocalTime getInicio();

    LocalTime getFim();
}
