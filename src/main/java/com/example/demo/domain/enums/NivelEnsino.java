package com.example.demo.domain.enums;

import lombok.Getter;

/**
 * Níveis de ensino disponíveis para uma disciplina.
 * Inclui uma descrição legível que pode ser utilizada nas respostas da API.
 */
@Getter
public enum NivelEnsino {
    MATERNAL("Maternal"),
    EDUCACAO_INFANTIL("Educação Infantil"),
    FUNDAMENTAL_ANOS_INICIAIS("Fundamental - Anos Iniciais"),
    FUNDAMENTAL_ANOS_FINAIS("Fundamental - Anos Finais"),
    ENSINO_MEDIO("Ensino Médio");

    private final String descricao;

    NivelEnsino(String descricao) {
        this.descricao = descricao;
    }
}
