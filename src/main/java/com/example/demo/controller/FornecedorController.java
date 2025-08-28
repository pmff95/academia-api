package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.FornecedorDTO;
import com.example.demo.service.FornecedorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Fornecedores")
@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {
    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody FornecedorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<Page<FornecedorDTO>>> listar(@RequestParam(required = false) String nome,
                                                                Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(nome, pageable)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiReturn<FornecedorDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PostMapping("/uuids")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<List<FornecedorDTO>>> buscarPorUuids(@RequestBody List<UUID> uuids) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuids(uuids)));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody FornecedorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Fornecedor removido"));
    }
}

