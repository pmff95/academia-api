package com.example.demo.dto.api.cobpag.normal;

import java.util.List;

public record ListarCobrancasResponse(
        Parametros parametros,
        List<BuscarCobrancaResponse> cobs
) {
}
