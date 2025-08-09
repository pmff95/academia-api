package com.example.demo.controller.academico.itinerario;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.AlunoGrupoItinerarioRequest;
import com.example.demo.dto.projection.AlunoGrupoItinerarioSummary;
import com.example.demo.service.academico.itinerario.AlunoGrupoItinerarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/itinerarios")
@Tag(name = "AlunosGrupoItinerario", description = "Gerenciamento de alunos dos grupos")
public class AlunoGrupoItinerarioController {

    private final AlunoGrupoItinerarioService service;

    public AlunoGrupoItinerarioController(AlunoGrupoItinerarioService service) {
        this.service = service;
    }

    @PostMapping("/{itinerarioUuid}/grupos/{grupoUuid}/alunos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Adicionar aluno", description = "Adiciona aluno ao grupo")
    public ResponseEntity<ApiReturn<String>> create(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @RequestBody @Valid AlunoGrupoItinerarioRequest request) {
        this.service.create(itinerarioUuid, grupoUuid, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Aluno cadastrado no grupo com sucesso."));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{grupoUuid}/alunos/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Atualizar aluno", description = "Atualiza aluno do grupo")
    public ResponseEntity<ApiReturn<String>> update(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid,
            @RequestBody @Valid AlunoGrupoItinerarioRequest request) {
        this.service.update(itinerarioUuid, grupoUuid, uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @GetMapping("/grupos/{grupoUuid}/alunos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar alunos", description = "Lista alunos do grupo")
    public ResponseEntity<ApiReturn<Page<AlunoGrupoItinerarioSummary>>> list(
            @PathVariable UUID grupoUuid,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findAll(grupoUuid, pageable)));
    }

    @GetMapping("/grupos/{grupoUuid}/alunos/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Buscar aluno", description = "Busca aluno do grupo pelo UUID")
    public ResponseEntity<ApiReturn<AlunoGrupoItinerarioSummary>> find(
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findByUuid(grupoUuid, uuid, AlunoGrupoItinerarioSummary.class)));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{grupoUuid}/alunos/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Ativar aluno", description = "Ativa aluno do grupo")
    public ResponseEntity<ApiReturn<String>> ativar(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        this.service.changeStatus(itinerarioUuid, grupoUuid, uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{grupoUuid}/alunos/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Inativar aluno", description = "Inativa aluno do grupo")
    public ResponseEntity<ApiReturn<String>> inativar(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        this.service.changeStatus(itinerarioUuid, grupoUuid, uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
    }
}
