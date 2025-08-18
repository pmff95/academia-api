package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AlimentoDTO {
    private UUID uuid;
    private String nome;
    private BigDecimal calorias;
    private BigDecimal gordura;
    private BigDecimal proteinas;
    private BigDecimal carboidratos;
    private BigDecimal acucares;
    private BigDecimal idr;
}
