package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.PatrocinadorDTO;
import com.example.demo.service.PatrocinadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Patrocinadores")
@RestController
@RequestMapping("/patrocinadores")
public class PatrocinadorController {
    private final PatrocinadorService service;

    public PatrocinadorController(PatrocinadorService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody PatrocinadorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<Page<PatrocinadorDTO>>> listar(@RequestParam(required = false) String nome,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(nome, pageable)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<PatrocinadorDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody PatrocinadorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Patrocinador removido"));
    }
}

