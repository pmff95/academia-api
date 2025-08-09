package com.example.demo.dto.ppp;

import java.time.LocalDate;
import java.util.UUID;

public record PppView(
        UUID uuid,
        String escolaNome,
        LocalDate inicioVigencia,
        LocalDate fimVigencia,
        LocalDate dataAprovacao,
        String responsavel
) {
}
