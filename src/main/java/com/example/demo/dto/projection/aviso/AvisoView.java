package com.example.demo.dto.projection.aviso;

import com.example.demo.domain.enums.Perfil;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AvisoView {
    UUID getUuid();

    String getTitulo();

    String getMensagem();

    LocalDateTime getCriadoEm();

    String getUsuarioNome();

    Perfil getUsuarioPerfil();
}
