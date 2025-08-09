package com.example.demo.dto.projection.aluno;

import com.example.demo.domain.enums.GrauParentesco;
import com.example.demo.dto.projection.usuario.UsuarioFull;

public interface ResponsavelAlunoSummary {
    UsuarioFull getResponsavel();

    GrauParentesco getGrauParentesco();
}
