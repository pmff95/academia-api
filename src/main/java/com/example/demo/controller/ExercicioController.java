package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ExercicioDTO;
import com.example.demo.service.ExercicioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Exercícios")
@RestController
@RequestMapping("/exercicios")
public class ExercicioController {
    private final ExercicioService service;

    public ExercicioController(ExercicioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> criar(@Validated @RequestBody ExercicioDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExercicioDTO>>> listar(Pageable pageable) {
        Page<ExercicioDTO> page = service.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de exercícios", page, null));
    }
}
