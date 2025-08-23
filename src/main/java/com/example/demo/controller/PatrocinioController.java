package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.PatrocinioDTO;
import com.example.demo.service.PatrocinioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Patrocínios")
@RestController
@RequestMapping("/patrocinios")
public class PatrocinioController {
    private final PatrocinioService service;

    public PatrocinioController(PatrocinioService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PATROCINADOR','MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> criar(@RequestPart("dto") PatrocinioDTO dto,
                                                   @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto, imagem)));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<Page<PatrocinioDTO>>> listar(@RequestParam UUID patrocinadorUuid,
                                                                 Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findByPatrocinador(patrocinadorUuid, pageable)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('PATROCINADOR','MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Patrocínio removido"));
    }
}

