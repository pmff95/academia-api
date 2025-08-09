package com.example.demo.dto.usuario;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;

public record UsuarioView(
        String nome,

        String email,

        Perfil perfil,

        Status status

) {
}
