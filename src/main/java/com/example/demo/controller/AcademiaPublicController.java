package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.AcademiaDTO;
import com.example.demo.service.AcademiaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Academias - Público")
@RestController
@RequestMapping("/api/public/academias")
public class AcademiaPublicController {
    private final AcademiaService service;

    public AcademiaPublicController(AcademiaService service) {
        this.service = service;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ApiReturn<AcademiaDTO>> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(ApiReturn.of(service.findByCodigo(codigo)));
    }
}
