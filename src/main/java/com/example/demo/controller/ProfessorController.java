package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> criar(@Validated @RequestBody ProfessorDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProfessorDTO>>> listar(@RequestParam(required = false) String nome,
                                                                  Pageable pageable) {
        Page<ProfessorDTO> page = service.findAll(nome, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de professores", page, null));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<ProfessorDTO>> buscar(@PathVariable UUID uuid) {
        ProfessorDTO dto = service.findByUuid(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Professor encontrado", dto, null));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody ProfessorDTO dto) {
        String msg = service.update(uuid, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Professor removido", null, null));
    }
}
