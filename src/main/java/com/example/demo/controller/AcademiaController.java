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
        String msg = service.create(dto);
        return ResponseEntity.ok(ApiReturn.of(msg));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<Page<AcademiaDTO>>> listar(@RequestParam(required = false) String nome,
                                                               Pageable pageable) {
        Page<AcademiaDTO> page = service.findAll(nome, pageable);
        return ResponseEntity.ok(ApiReturn.of(page));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<AcademiaDTO>> buscar(@PathVariable UUID uuid) {
        AcademiaDTO dto = service.findByUuid(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Academia encontrada", dto, null));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> atualizar(@PathVariable UUID uuid,
                                                         @Validated @RequestBody AcademiaDTO dto) {
        String msg = service.update(uuid, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }
}
