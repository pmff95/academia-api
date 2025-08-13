package com.example.demo.controller;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.service.AcademiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

@Tag(name = "Academias")
@RestController
@RequestMapping("/academias")
@PreAuthorize("hasAnyRole('MASTER','ADMIN')")
public class AcademiaController {
    private final AcademiaService service;

    public AcademiaController(AcademiaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody AcademiaDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<Page<AcademiaDTO>>> listar(@RequestParam(required = false) String nome,
                                                               Pageable pageable) {
        Page<AcademiaDTO> page = service.findAll(nome, pageable);
        return ResponseEntity.ok(ApiReturn.of(page));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiReturn<AcademiaDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid,
                                                       @Validated @RequestBody AcademiaDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }
}
