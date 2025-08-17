package com.example.demo.controller;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.service.AlunoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alunos - Público")
@RestController
@RequestMapping("/api/public/alunos")
public class AlunoPublicController {
    private final AlunoService service;

    public AlunoPublicController(AlunoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }
}
