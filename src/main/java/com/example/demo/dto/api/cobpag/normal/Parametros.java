package com.example.demo.dto.api.cobpag.normal;

import java.time.OffsetDateTime;

public record Parametros(
        OffsetDateTime inicio,
        OffsetDateTime fim,
        Paginacao paginacao
) {
}
