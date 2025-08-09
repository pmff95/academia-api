package com.example.demo.dto.projection;

import com.example.demo.domain.enums.Status;
import java.util.UUID;

public interface ProfessorSummary {
    UUID getUuid();

    String getNome();

    String getFoto();

    String getEmail();

    Status getStatus();
}
