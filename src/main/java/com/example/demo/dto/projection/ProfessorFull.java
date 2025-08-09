package com.example.demo.dto.projection;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.enums.GrupoSanguineo;
import java.time.LocalDate;

import java.util.List;
import java.util.UUID;

/**
 * Detailed information about a professor used when searching by UUID.
 */
public interface ProfessorFull {

    Long getId();

    UUID getUuid();

    String getNome();

    String getEmail();

    Status getStatus();

    String getCpf();

    String getFoto();

    String getMatricula();

    String getTelefone();

    GrupoSanguineo getGrupoSanguineo();

    LocalDate getDataNascimento();

    Sexo getSexo();

    String getPai();

    String getMae();

    List<HorarioDisponivelSummary> getHorariosDisponiveis();

    List<DisciplinaSummary> getDisciplinas();
}
