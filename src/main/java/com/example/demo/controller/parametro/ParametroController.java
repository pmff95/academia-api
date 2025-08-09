package com.example.demo.controller.parametro;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.parametro.ParametroDTO;
import com.example.demo.dto.parametro.ParametroEscolaDTO;
import com.example.demo.dto.parametro.SimplesParametroValorView;
import com.example.demo.repository.specification.ParametroSpecification;
import com.example.demo.dto.parametro.ParamsView;
import com.example.demo.service.parametro.ParametroService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Parâmetros", description = "Gerenciamento de parâmetros")
public class ParametroController {

    private final ParametroService service;

    public ParametroController(ParametroService service) {
        this.service = service;
    }

    @GetMapping("/parametros")
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Listar parâmetros",
            description = "Lista parâmetros globais filtrando por chave, descrição, tipo e status"
    )
    public ResponseEntity<ApiReturn<List<ParamsView>>> listar(
            @ParameterObject ParametroSpecification specification) {
        return ResponseEntity.ok(ApiReturn.of(service.listarParametros(specification)));
    }

    @GetMapping("/parametros/{chave}/{error}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Listar parâmetros",
            description = "Lista parâmetros globais filtrando por chave, descrição, tipo e status"
    )
    public ResponseEntity<ApiReturn<SimplesParametroValorView>> retornaValorParametro(
            @Parameter(description = "Chave do parâmetro a ser buscado", required = true)
            @PathVariable("chave") String chave,

            @Parameter(description = "Deseja que estourar erro em caso não encontre?", required = true)
            @PathVariable("error") boolean error
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.retornaValorParametro(chave, error)));
    }

    @PostMapping("/parametros")
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Criar ou editar parâmetro global",
            description = "Cria ou atualiza um parâmetro global"
    )
    public ResponseEntity<ApiReturn<String>> criarOuEditarParametroGlobal(
            @RequestBody @Valid ParametroDTO dto) {
        service.criarOuEditarParametroGlobal(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Parâmetro salvo com sucesso."));
    }

    @PostMapping("/escola/parametros")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Criar ou editar parâmetro da escola",
            description = "Cria ou atualiza um parâmetro customizado para a escola"
    )
    public ResponseEntity<ApiReturn<Object>> criarOuEditarParametroEscola(
            @RequestBody @Valid ParametroEscolaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of(service.criarOuEditarParametroEscola(dto)));
    }

    @GetMapping("/escola/parametros")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Listar parâmetros da escola",
            description = "Lista os parâmetros visíveis para a escola"
    )
    public ResponseEntity<ApiReturn<List<ParamsView>>> listarParametros() {
        return ResponseEntity.ok(ApiReturn.of(service.listarParametrosPorEscola()));
    }

    @PutMapping("/escola/parametros/{chave}/status")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Alterar status do parâmetro da escola",
            description = "Alterna entre ativo e inativo um parâmetro da escola"
    )
    public ResponseEntity<ApiReturn<String>> alterarStatusParametroEscola(
            @PathVariable("chave") String chave) {
        service.toggleParametroEscolaStatus(chave);
        return ResponseEntity.ok(ApiReturn.of("Status alterado com sucesso."));
    }

    @PutMapping("/parametros/{chave}/status")
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Alterar status do parâmetro",
            description = "Alterna entre ativo e inativo um parâmetro global e reflete para as escolas"
    )
    public ResponseEntity<ApiReturn<String>> alterarStatusParametro(
            @PathVariable("chave") String chave) {
        service.toggleParametroStatus(chave);
        return ResponseEntity.ok(ApiReturn.of("Status alterado com sucesso."));
    }
}
