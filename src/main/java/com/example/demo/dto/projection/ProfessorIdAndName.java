package com.example.demo.dto.projection;

import java.util.UUID;

/**
 * Simplified view of Professor containing only identifier and name.
 */
public interface ProfessorIdAndName {
    UUID getUuid();

    String getNome();
}
