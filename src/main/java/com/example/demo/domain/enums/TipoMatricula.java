package com.example.demo.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TipoMatricula {

    ALUNO('A'),
    RESPONSAVEL('R'),
    PROFESSOR('P'),
    FUNCIONARIO('F');

    @Getter
    private final char sigla;

    public static TipoMatricula fromCodigo(char codigo) {
        for (TipoMatricula tipo : values()) {
            if (tipo.sigla == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoMatricula: " + codigo);
    }
}

