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
import java.util.UUID;

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

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<AlunoDTO>> buscar(@PathVariable UUID uuid) {
        AlunoDTO dto = service.findByUuid(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno encontrado", dto, null));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody AlunoDTO dto) {
        String msg = service.update(uuid, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Aluno removido", null, null));
    }

    @PostMapping("/{uuid}/medidas")
    public ResponseEntity<ApiResponse<String>> adicionarMedida(@PathVariable UUID uuid,
                                                               @Validated @RequestBody AlunoMedidaDTO dto) {
        String msg = medidaService.adicionarMedida(uuid, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping("/{uuid}/medidas")
    public ResponseEntity<ApiResponse<List<AlunoMedidaDTO>>> listarMedidas(@PathVariable UUID uuid) {
        List<AlunoMedidaDTO> lista = medidaService.listarMedidas(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de medidas", lista, null));
    }

    @PostMapping("/{uuid}/observacoes")
    public ResponseEntity<ApiResponse<String>> adicionarObservacao(@PathVariable UUID uuid,
                                                                   @Validated @RequestBody AlunoObservacaoDTO dto) {
        String msg = observacaoService.adicionarObservacao(uuid, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping("/{uuid}/observacoes")
    public ResponseEntity<ApiResponse<List<AlunoObservacaoDTO>>> listarObservacoes(@PathVariable UUID uuid) {
        List<AlunoObservacaoDTO> lista = observacaoService.listarObservacoes(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de observações", lista, null));
    }
}
