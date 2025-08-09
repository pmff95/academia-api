package com.example.demo.controller.academico.itinerario;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.DisciplinaGrupoItinerarioRequest;
import com.example.demo.dto.projection.DisciplinaGrupoItinerarioIdAndName;
import com.example.demo.dto.projection.DisciplinaGrupoItinerarioSummary;
import com.example.demo.service.academico.itinerario.DisciplinaGrupoItinerarioService;
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
@RequestMapping("/api/itinerarios")
@Tag(name = "DisciplinasGrupoItinerario", description = "Gerenciamento de disciplinas dos grupos")
public class DisciplinaGrupoItinerarioController {

    private final DisciplinaGrupoItinerarioService service;

    public DisciplinaGrupoItinerarioController(DisciplinaGrupoItinerarioService service) {
        this.service = service;
    }

    @PostMapping("/{itinerarioUuid}/grupos/{grupoUuid}/disciplinas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Adicionar disciplina", description = "Adiciona disciplina ao grupo")
    public ResponseEntity<ApiReturn<String>> create(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @RequestBody @Valid DisciplinaGrupoItinerarioRequest request) {
        this.service.create(itinerarioUuid, grupoUuid, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Disciplina cadastrada com sucesso."));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{grupoUuid}/disciplinas/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Atualizar disciplina", description = "Atualiza disciplina do grupo")
    public ResponseEntity<ApiReturn<String>> update(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid,
            @RequestBody @Valid DisciplinaGrupoItinerarioRequest request) {
        this.service.update(itinerarioUuid, grupoUuid, uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @GetMapping("/grupos/{grupoUuid}/disciplinas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar disciplinas", description = "Lista disciplinas do grupo")
    public ResponseEntity<ApiReturn<Page<DisciplinaGrupoItinerarioSummary>>> list(
            @PathVariable UUID grupoUuid,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findAll(grupoUuid, pageable)));
    }

    @GetMapping("/grupos/{grupoUuid}/disciplinas/combobox")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar disciplinas", description = "Lista disciplinas do grupo (nome e uuid)")
    public ResponseEntity<ApiReturn<List<DisciplinaGrupoItinerarioIdAndName>>> listCombobox(
            @PathVariable UUID grupoUuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service
                .findAll(grupoUuid, DisciplinaGrupoItinerarioIdAndName.class)));
    }

    @GetMapping("/grupos/{grupoUuid}/disciplinas/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Buscar disciplina", description = "Busca disciplina do grupo pelo UUID")
    public ResponseEntity<ApiReturn<DisciplinaGrupoItinerarioSummary>> find(
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findByUuid(grupoUuid, uuid, DisciplinaGrupoItinerarioSummary.class)));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{grupoUuid}/disciplinas/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Ativar disciplina", description = "Ativa disciplina do grupo")
    public ResponseEntity<ApiReturn<String>> ativar(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        this.service.changeStatus(itinerarioUuid, grupoUuid, uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{grupoUuid}/disciplinas/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Inativar disciplina", description = "Inativa disciplina do grupo")
    public ResponseEntity<ApiReturn<String>> inativar(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        this.service.changeStatus(itinerarioUuid, grupoUuid, uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
    }

    @DeleteMapping("/{itinerarioUuid}/grupos/{grupoUuid}/disciplinas/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Remover disciplina", description = "Remove disciplina do grupo")
    public ResponseEntity<ApiReturn<String>> delete(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID grupoUuid,
            @PathVariable UUID uuid) {
        this.service.delete(itinerarioUuid, grupoUuid, uuid);
        return ResponseEntity.ok(ApiReturn.of("Removido com sucesso."));
    }
}
