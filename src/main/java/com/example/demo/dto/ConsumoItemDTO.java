package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ConsumoItemDTO {
    private UUID alimentoUuid;
    private BigDecimal quantidade;
}
