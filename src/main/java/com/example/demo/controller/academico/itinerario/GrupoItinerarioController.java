package com.example.demo.controller.academico.itinerario;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.GrupoItinerarioRequest;
import com.example.demo.dto.projection.GrupoItinerarioSummary;
import com.example.demo.service.academico.itinerario.GrupoItinerarioService;
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
@Tag(name = "GruposItinerario", description = "Gerenciamento de grupos do itinerário")
public class GrupoItinerarioController {

    private final GrupoItinerarioService service;

    public GrupoItinerarioController(GrupoItinerarioService service) {
        this.service = service;
    }

    @PostMapping("/{itinerarioUuid}/grupos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Criar grupo", description = "Cria grupo em um itinerário")
    public ResponseEntity<ApiReturn<String>> create(
            @PathVariable UUID itinerarioUuid,
            @RequestBody @Valid GrupoItinerarioRequest request) {
        this.service.create(itinerarioUuid, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Grupo cadastrado com sucesso."));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Atualizar grupo", description = "Atualiza grupo do itinerário")
    public ResponseEntity<ApiReturn<String>> update(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID uuid,
            @RequestBody @Valid GrupoItinerarioRequest request) {
        this.service.update(itinerarioUuid, uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @GetMapping("/{itinerarioUuid}/grupos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar grupos", description = "Lista grupos do itinerário")
    public ResponseEntity<ApiReturn<Page<GrupoItinerarioSummary>>> list(
            @PathVariable UUID itinerarioUuid,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findAll(itinerarioUuid, pageable)));
    }

    @GetMapping("/{itinerarioUuid}/grupos/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Buscar grupo", description = "Busca grupo pelo UUID")
    public ResponseEntity<ApiReturn<GrupoItinerarioSummary>> find(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findByUuid(itinerarioUuid, uuid, GrupoItinerarioSummary.class)));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Ativar grupo", description = "Ativa grupo do itinerário")
    public ResponseEntity<ApiReturn<String>> ativar(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID uuid) {
        this.service.changeStatus(itinerarioUuid, uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
    }

    @PutMapping("/{itinerarioUuid}/grupos/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Inativar grupo", description = "Inativa grupo do itinerário")
    public ResponseEntity<ApiReturn<String>> inativar(
            @PathVariable UUID itinerarioUuid,
            @PathVariable UUID uuid) {
        this.service.changeStatus(itinerarioUuid, uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
    }
}
