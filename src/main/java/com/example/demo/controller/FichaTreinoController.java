package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.service.FichaTreinoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fichas")
public class FichaTreinoController {
    private final FichaTreinoService service;

    public FichaTreinoController(FichaTreinoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> criar(@Validated @RequestBody FichaTreinoDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FichaTreinoDTO>>> listar(Pageable pageable) {
        Page<FichaTreinoDTO> page = service.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de fichas de treino", page, null));
    }
}
