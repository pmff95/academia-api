package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.ProfessorDTO;
import com.example.demo.service.AlunoService;
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
public class ProfessorController {
    private final ProfessorService service;
    private final AlunoService alunoService;

    public ProfessorController(ProfessorService service, AlunoService alunoService) {
        this.service = service;
        this.alunoService = alunoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<Page<ProfessorDTO>>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) UUID alunoUuid,
            @RequestParam(required = false) Boolean possuiCref,
            Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(nome, alunoUuid, possuiCref, pageable)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<ProfessorDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Professor removido"));
    }

    @GetMapping("/alunos")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<Page<AlunoDTO>>> listarAlunosVinculados(
            @RequestParam(required = false) String nome,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiReturn.of(alunoService.findAllFromLoggedProfessor(nome, pageable)));
    }
}