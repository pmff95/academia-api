package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.ProfessorDTO;
import com.example.demo.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;

@Tag(name = "Professores")
@RestController
@RequestMapping("/professores")
@PreAuthorize("hasAnyRole('MASTER','ADMIN')")
public class ProfessorController {
    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<Page<ProfessorDTO>>> listar(@RequestParam(required = false) String nome,
                                                                Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(nome, pageable)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiReturn<ProfessorDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Professor removido"));
    }
}
