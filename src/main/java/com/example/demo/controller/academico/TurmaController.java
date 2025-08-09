package com.example.demo.controller.academico;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.academico.TurmaRequest;
import com.example.demo.dto.academico.TurmaSerieRequest;
import com.example.demo.dto.projection.TurmaSerieView;
import com.example.demo.dto.projection.TurmaSummary;
import com.example.demo.dto.projection.aluno.AlunoSummary;
import com.example.demo.service.academico.TurmaService;
import com.example.demo.repository.specification.TurmaSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/turmas")
@Tag(name = "Turmas", description = "Endpoints para gerenciamento de turmas")
public class TurmaController {

    private final TurmaService service;

    public TurmaController(TurmaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Criar uma turma",
            description = "Cria e persiste uma nova turma."
    )
    public ResponseEntity<ApiReturn<String>> create(@RequestBody @Valid TurmaRequest request) {
        this.service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Turma cadastrada com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Atualizar turma",
            description = "Atualiza, a partir do seu UUID, uma turma persistida com as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> update(@PathVariable UUID uuid, @RequestBody @Valid TurmaRequest request) {
        this.service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Turma atualizada com sucesso."));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Listar turmas",
            description = "Retorna uma lista paginada de turmas."
    )
    public ResponseEntity<ApiReturn<Page<TurmaSummary>>> findAll(
            @ParameterObject TurmaSpecification specification,
            @ParameterObject Pageable pageable) {
        Page<TurmaSummary> page = this.service.findAll(specification, pageable);
        return ResponseEntity.ok(ApiReturn.of(page));
    }

    @PostMapping("/por-serie")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Listar turmas por série",
            description = "Retorna as turmas correspondentes à série informada."
    )
    public ResponseEntity<ApiReturn<List<TurmaSerieView>>> findBySerie(@RequestBody @Valid TurmaSerieRequest request) {
        var turmas = this.service.findBySerie(request.serie(), TurmaSerieView.class);
        return ResponseEntity.ok(ApiReturn.of(turmas));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Buscar turma por UUID",
            description = "Retorna os dados de uma turma pelo seu UUID."
    )
    public ResponseEntity<ApiReturn<TurmaSummary>> findByUuid(@PathVariable UUID uuid) {
        TurmaSummary turma = this.service.findByUuid(uuid, TurmaSummary.class);
        return ResponseEntity.ok(ApiReturn.of(turma));
    }

    @GetMapping("/{uuid}/alunos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(
            summary = "Listar alunos da turma",
            description = "Retorna os alunos pertencentes à turma."
    )
    public ResponseEntity<ApiReturn<List<AlunoSummary>>> listarAlunos(@PathVariable("uuid") UUID uuid) {
        List<AlunoSummary> alunos = this.service.listarAlunos(uuid);
        return ResponseEntity.ok(ApiReturn.of(alunos));
    }

}
