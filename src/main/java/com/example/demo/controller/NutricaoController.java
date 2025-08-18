package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.AlimentoDTO;
import com.example.demo.dto.ConsumoDiarioDTO;
import com.example.demo.service.AlimentoService;
import com.example.demo.service.ConsumoAlimentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Nutrição")
@RestController
@RequestMapping("/nutricao")
public class NutricaoController {
    private final AlimentoService alimentoService;
    private final ConsumoAlimentoService consumoService;

    public NutricaoController(AlimentoService alimentoService, ConsumoAlimentoService consumoService) {
        this.alimentoService = alimentoService;
        this.consumoService = consumoService;
    }

    @PostMapping("/alimentos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> criarAlimento(@Validated @RequestBody AlimentoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(alimentoService.create(dto)));
    }

    @PostMapping("/consumos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR','ALUNO')")
    public ResponseEntity<ApiReturn<String>> registrarConsumo(@Validated @RequestBody ConsumoDiarioDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(consumoService.registrar(dto)));
    }
}
