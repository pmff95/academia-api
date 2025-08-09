package com.example.demo.dto.instituicao;

import com.example.demo.dto.projection.escola.EscolaView;
import com.example.demo.dto.usuario.UsuarioView;
import org.springframework.data.domain.Page;

public record EscolaUsuariosView(
        EscolaView escola, Page<UsuarioView> usuarios
) {
}
