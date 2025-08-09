package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CriarLoteCobrancaVencimentoRequest(
        String descricao,
        List<CriarCobrancaVencimentoRequest> cobsv
) {}