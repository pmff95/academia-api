package com.example.demo.dto.projection.aluno;

import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.enums.Status;
import java.time.LocalDate;
import com.example.demo.dto.projection.TurmaSummary;
import com.example.demo.dto.projection.escola.EscolaIdAndName;

import java.util.List;
import java.util.UUID;

/**
 * Detailed information about a student used when searching by UUID.
 */
public interface AlunoFull {
    EscolaIdAndName getEscola();

    List<ResponsavelAlunoSummary> getResponsaveis();

    List<TurmaSummary> getTurmas();

    UUID getUuid();

    String getNome();

    Status getStatus();

    String getEmail();

    String getCpf();

    Sexo getSexo();

    String getTelefone();

    GrupoSanguineo getGrupoSanguineo();

    LocalDate getDataNascimento();

    String getMatricula();

    String getFoto();
}
