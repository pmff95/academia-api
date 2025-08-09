package com.example.demo.dto.projection.aluno;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Informações resumidas de um aluno utilizadas em listagens.
 */
public interface AlunoSummary {

    UUID getUuid();
    String getNome();
    String getFoto();
    String getMatricula();
    String getCpf();
    LocalDate getDataNascimento();
    Serie getSerie();
    Status getStatus();
}

