package com.example.demo.dto.projection;

import java.time.LocalDate;
import java.util.UUID;

public interface RegistroChamadaSummary {
    UUID getUuid();

    LocalDate getDataAula();

    Boolean getAulaDupla();
}
