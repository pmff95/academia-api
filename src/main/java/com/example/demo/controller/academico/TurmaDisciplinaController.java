package com.example.demo.controller.academico;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.TurmaDisciplinaRequest;
import com.example.demo.dto.projection.TurmaDisciplinaIdAndName;
import com.example.demo.service.academico.TurmaDisciplinaService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/turma-disciplina")
@Tag(name = "TurmaDisciplinas", description = "Endpoints para gerenciamento de disciplinas nas turmas")
public class TurmaDisciplinaController {

    private final TurmaDisciplinaService service;

    public TurmaDisciplinaController(TurmaDisciplinaService service) {
        this.service = service;
    }

    @PutMapping("/{turmaUuid}/disciplinas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Salvar disciplinas da turma",
            description = "Cria ou atualiza vínculos de disciplinas da turma")
    public ResponseEntity<ApiReturn<String>> saveAll(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Disciplinas vinculadas à turma",
                    required = true
            )
            @RequestBody @Valid List<TurmaDisciplinaRequest> requests) {
        this.service.saveAll(turmaUuid, requests);
        return ResponseEntity.ok(ApiReturn.of("Processado com sucesso."));
    }

    @GetMapping("/{turmaUuid}/disciplinas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar disciplinas da turma",
            description = "Retorna disciplinas vinculadas à turma")
    public ResponseEntity<ApiReturn<List<TurmaDisciplinaIdAndName>>> list(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service
                .findAll(turmaUuid, TurmaDisciplinaIdAndName.class)));
    }

    @GetMapping("/{turmaUuid}/disciplinas/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Buscar disciplina da turma",
            description = "Busca disciplina vinculada à turma pelo UUID")
    public ResponseEntity<ApiReturn<TurmaDisciplinaIdAndName>> find(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid,
            @Parameter(description = "UUID do vínculo", required = true)
            @PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service
                .findByUuid(turmaUuid, uuid, TurmaDisciplinaIdAndName.class)));
    }

    @PutMapping("/{turmaUuid}/disciplinas/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Inativar disciplina da turma",
            description = "Inativa vínculo de disciplina da turma")
    public ResponseEntity<ApiReturn<String>> inativar(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid,
            @Parameter(description = "UUID do vínculo", required = true)
            @PathVariable UUID uuid) {
        this.service.changeStatus(turmaUuid, uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
    }

    @PutMapping("/{turmaUuid}/disciplinas/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Ativar disciplina da turma",
            description = "Ativa vínculo de disciplina da turma")
    public ResponseEntity<ApiReturn<String>> ativar(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid,
            @Parameter(description = "UUID do vínculo", required = true)
            @PathVariable UUID uuid) {
        this.service.changeStatus(turmaUuid, uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
    }
}
