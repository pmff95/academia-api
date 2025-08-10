package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AlunoMedidaService;
import com.example.demo.service.AlunoObservacaoService;
import com.example.demo.service.AlunoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Alunos")
@Slf4j
@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService service;
    private final AlunoMedidaService medidaService;
    private final AlunoObservacaoService observacaoService;

    public AlunoController(AlunoService service,
                           AlunoMedidaService medidaService,
                           AlunoObservacaoService observacaoService) {
        this.service = service;
        this.medidaService = medidaService;
        this.observacaoService = observacaoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> criar(@Validated @RequestBody AlunoDTO dto) {
        String msg = service.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
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
    public ResponseEntity<ApiResponse<String>> atualizar(@PathVariable Long id, @Validated @RequestBody AlunoDTO dto) {
        String msg = service.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno removido", null, null));
    }

    @PostMapping("/{id}/medidas")
    public ResponseEntity<ApiResponse<String>> adicionarMedida(@PathVariable Long id,
                                                               @Validated @RequestBody AlunoMedidaDTO dto) {
        String msg = medidaService.adicionarMedida(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping("/{id}/medidas")
    public ResponseEntity<ApiResponse<List<AlunoMedidaDTO>>> listarMedidas(@PathVariable Long id) {
        List<AlunoMedidaDTO> lista = medidaService.listarMedidas(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de medidas", lista, null));
    }

    @PostMapping("/{id}/observacoes")
    public ResponseEntity<ApiResponse<String>> adicionarObservacao(@PathVariable Long id,
                                                                   @Validated @RequestBody AlunoObservacaoDTO dto) {
        String msg = observacaoService.adicionarObservacao(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping("/{id}/observacoes")
    public ResponseEntity<ApiResponse<List<AlunoObservacaoDTO>>> listarObservacoes(@PathVariable Long id) {
        List<AlunoObservacaoDTO> lista = observacaoService.listarObservacoes(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de observações", lista, null));
    }
}
