package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Musculo;
import com.example.demo.service.ExercicioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Exercícios")
@RestController
@RequestMapping("/exercicios")
@PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
public class ExercicioController {
    private final ExercicioService service;

    public ExercicioController(ExercicioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody ExercicioDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(ApiReturn.of(msg));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<Page<ExercicioDTO>>> listar(@RequestParam(required = false) String nome,
                                                                @RequestParam(required = false) Musculo musculo,
                                                                Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.find(nome, musculo, pageable)));
    }
}
