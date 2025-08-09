package com.example.demo.controller;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.ApiResponse;
import com.example.demo.service.AlunoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AlunoDTO>> criar(@Validated @RequestBody AlunoDTO dto) {
        AlunoDTO salvo = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno criado com sucesso", salvo, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AlunoDTO>>> listar(Pageable pageable) {
        Page<AlunoDTO> page = service.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de alunos", page, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoDTO>> buscar(@PathVariable Long id) {
        AlunoDTO dto = service.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno encontrado", dto, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoDTO>> atualizar(@PathVariable Long id, @Validated @RequestBody AlunoDTO dto) {
        AlunoDTO atualizado = service.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno atualizado", atualizado, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno removido", null, null));
    }
}
