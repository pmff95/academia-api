package com.example.demo.controller;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.dto.ApiResponse;
import com.example.demo.service.AcademiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public ResponseEntity<ApiResponse<String>> criar(@Validated @RequestBody AcademiaDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AcademiaDTO>>> listar(@RequestParam(required = false) String nome,
                                                                 Pageable pageable) {
        Page<AcademiaDTO> page = service.findAll(nome, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de academias", page, null));
    }
}
