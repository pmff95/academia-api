package com.example.demo.dto.projection;

import java.util.UUID;

/**
 * Simplified view of Disciplina containing only identifier and name.
 */
public interface DisciplinaIdAndName {
    UUID getUuid();

    String getNome();
}
