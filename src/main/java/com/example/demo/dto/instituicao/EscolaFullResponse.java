package com.example.demo.dto.instituicao;

import com.example.demo.domain.enums.Status;
import com.example.demo.dto.projection.usuario.UsuarioFull;

import java.time.LocalDateTime;
import java.util.UUID;

public record EscolaFullResponse(
        UUID uuid,
        String nome,
        String nomeCurto,
        String cnpj,
        Status status,
        LocalDateTime criadoEm,
        EscolaEnderecoResponse endereco,
        EscolaFinanceiroResponse financeiro,
        UsuarioFull admin
) {
}
