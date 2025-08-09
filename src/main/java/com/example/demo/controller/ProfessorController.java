package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ProfessorDTO;
import com.example.demo.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professores")
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
    public ResponseEntity<ApiResponse<Page<ProfessorDTO>>> listar(Pageable pageable) {
        Page<ProfessorDTO> page = service.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de professores", page, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfessorDTO>> buscar(@PathVariable Long id) {
        ProfessorDTO dto = service.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Professor encontrado", dto, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> atualizar(@PathVariable Long id, @Validated @RequestBody ProfessorDTO dto) {
        String msg = service.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Professor removido", null, null));
    }
}
