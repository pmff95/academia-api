package com.example.demo.dto.instituicao;

import com.example.demo.domain.enums.NomeModulo;
import com.example.demo.domain.model.instituicao.EscolaModulo;

import java.time.LocalDate;

/**
 * Representa um módulo ativo para uma escola.
 */
public record ModuloResponse(
        Long id,
        NomeModulo modulo,
        boolean ativo,
        LocalDate dataAtivacao,
        LocalDate dataExpiracao
) {
    public static ModuloResponse fromEntity(EscolaModulo entity) {
        if (entity == null) {
            return null;
        }
        return new ModuloResponse(
                entity.getId(),
                entity.getModulo(),
                entity.isAtivo(),
                entity.getDataAtivacao(),
                entity.getDataExpiracao()
        );
    }
}
