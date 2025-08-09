package com.example.demo.dto.projection.usuario;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.GrupoSanguineo;

import java.time.LocalDate;
import com.example.demo.dto.projection.escola.EscolaIdAndName;

import java.util.UUID;

public interface UsuarioFull {
    EscolaIdAndName getEscola();

    UUID getUuid();

    String getNome();

    String getEmail();

    String getTelefone();

    String getFoto();

    String getCpf();

    Perfil getPerfil();

    Status getStatus();

    GrupoSanguineo getGrupoSanguineo();

    LocalDate getDataNascimento();
}
