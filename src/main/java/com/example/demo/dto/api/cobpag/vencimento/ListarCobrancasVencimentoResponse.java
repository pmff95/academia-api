package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListarCobrancasVencimentoResponse(
    ParametrosConsulta parametros,
    List<BuscarCobrancaVencimentoResponse> cobs
) {}