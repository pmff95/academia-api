package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.service.FichaTreinoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Fichas de Treino")
@RestController
@RequestMapping("/fichas")
@PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
public class FichaTreinoController {
    private final FichaTreinoService service;

    public FichaTreinoController(FichaTreinoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> criar(@Validated @RequestBody FichaTreinoDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FichaTreinoDTO>>> listar(Pageable pageable) {
        Page<FichaTreinoDTO> page = service.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de fichas de treino", page, null));
    }

    @GetMapping("/aluno/{alunoUuid}")
    public ResponseEntity<ApiResponse<List<FichaTreinoDTO>>> listarPorAluno(@PathVariable UUID alunoUuid) {
        List<FichaTreinoDTO> fichas = service.findByAluno(alunoUuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de fichas do aluno", fichas, null));
    }
}
