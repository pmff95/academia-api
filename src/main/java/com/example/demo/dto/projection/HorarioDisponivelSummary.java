package com.example.demo.dto.projection;

import com.example.demo.domain.enums.DiaSemana;
import com.example.demo.domain.enums.Turno;

import java.time.LocalTime;
import java.util.UUID;

public interface HorarioDisponivelSummary {
    UUID getUuid();

    DiaSemana getDia();

    LocalTime getInicio();

    LocalTime getFim();

    Turno getTurno();
}
