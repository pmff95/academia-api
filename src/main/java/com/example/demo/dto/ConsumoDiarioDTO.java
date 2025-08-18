package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ConsumoDiarioDTO {
    private LocalDate data;
    private List<ConsumoItemDTO> lancheManha;
    private List<ConsumoItemDTO> almoco;
    private List<ConsumoItemDTO> lancheTarde;
    private List<ConsumoItemDTO> janta;
    private List<ConsumoItemDTO> ceia;
}
