package com.example.demo.controller.academico.itinerario;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.academico.ItinerarioFormativoRequest;
import com.example.demo.dto.projection.ItinerarioFormativoSummary;
import com.example.demo.service.academico.itinerario.ItinerarioFormativoService;
import com.example.demo.repository.specification.ItinerarioFormativoSpecification;
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
@Tag(name = "Itinerarios", description = "Endpoints para gerenciamento de itinerários")
public class ItinerarioFormativoController {

    private final ItinerarioFormativoService service;

    public ItinerarioFormativoController(ItinerarioFormativoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Criar itinerário", description = "Cria um novo itinerário formativo")
    public ResponseEntity<ApiReturn<String>> create(@RequestBody @Valid ItinerarioFormativoRequest request) {
        this.service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Itinerário cadastrado com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Atualizar itinerário", description = "Atualiza itinerário pelo UUID")
    public ResponseEntity<ApiReturn<String>> update(@PathVariable UUID uuid, @RequestBody @Valid ItinerarioFormativoRequest request) {
        this.service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar itinerários", description = "Lista itinerários formativos")
    public ResponseEntity<ApiReturn<Page<ItinerarioFormativoSummary>>> list(
            @ParameterObject ItinerarioFormativoSpecification specification,
            @ParameterObject Pageable pageable) {
        Page<ItinerarioFormativoSummary> page = this.service.findAll(specification, pageable);
        return ResponseEntity.ok(ApiReturn.of(page));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Buscar itinerário", description = "Busca itinerário pelo UUID")
    public ResponseEntity<ApiReturn<ItinerarioFormativoSummary>> find(@PathVariable UUID uuid) {
        ItinerarioFormativoSummary summary = this.service.findByUuid(uuid, ItinerarioFormativoSummary.class);
        return ResponseEntity.ok(ApiReturn.of(summary));
    }
}
