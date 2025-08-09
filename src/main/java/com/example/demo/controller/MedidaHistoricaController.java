package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.MedidaHistoricaDTO;
import com.example.demo.service.MedidaHistoricaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/alunos/{alunoId}/medidas")
public class MedidaHistoricaController {
    private final MedidaHistoricaService service;

    public MedidaHistoricaController(MedidaHistoricaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedidaHistoricaDTO>> criar(@PathVariable Long alunoId,
                                                                 @Validated @RequestBody MedidaHistoricaDTO dto) {
        MedidaHistoricaDTO salvo = service.create(alunoId, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Medida registrada", salvo, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MedidaHistoricaDTO>>> listar(@PathVariable Long alunoId,
                                                                       Pageable pageable) {
        Page<MedidaHistoricaDTO> page = service.listByAluno(alunoId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Histórico de medidas", page, null));
    }
}
