package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.GrauParentesco;
import com.example.demo.dto.usuario.UsuarioRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record ResponsavelAlunoRequest(
        @Schema(description = "Grau de parentesco do responsável")
        GrauParentesco grauParentesco,

        @Schema(description = "Dados do responsável")
        UsuarioRequest responsavel,

        @Schema(description = "Grau de parentesco do segundo responsável")
        GrauParentesco grauParentescoResponsavel2,

        @Schema(description = "Dados do segundo responsável")
        UsuarioRequest responsavel2

) {

}
