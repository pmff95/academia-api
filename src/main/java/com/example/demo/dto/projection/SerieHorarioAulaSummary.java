package com.example.demo.dto.projection;

import java.time.LocalTime;
import java.util.UUID;

public interface SerieHorarioAulaSummary {
    UUID getUuid();

    Integer getOrdem();

    LocalTime getInicio();

    LocalTime getFim();

    Boolean getIntervalo();
}
