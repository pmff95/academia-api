package com.example.demo.dto.projection;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Turno;

import java.util.UUID;

public interface TurmaSerieView {
    UUID getUuid();

    String getNome();

    Serie getSerie();

    Long getQuantidadeAlunos();

    Turno getTurno();

    Integer getLimiteVagas();
}
