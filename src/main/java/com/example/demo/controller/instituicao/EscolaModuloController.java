package com.example.demo.controller.instituicao;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.dto.instituicao.ModuloResponse;
import com.example.demo.dto.instituicao.AtivarModuloRequest;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.service.instituicao.EscolaModuloService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/modulos")
@Tag(name = "Módulos da Escola", description = "Gerenciamento de módulos da escola")
public class EscolaModuloController {

    private final EscolaModuloService escolaModuloService;

    public EscolaModuloController(EscolaModuloService escolaModuloService) {
        this.escolaModuloService = escolaModuloService;
    }

    @GetMapping("/{escolaUuid}")
    @EurekaApiOperation(
            summary = "Listar módulos da escola",
            description = "Retorna os módulos ativos para a escola informada."
    )
    public ResponseEntity<ApiReturn<List<ModuloResponse>>> listar(
            @Parameter(description = "UUID da escola", required = true)
            @PathVariable UUID escolaUuid) {
        List<ModuloResponse> modulos = escolaModuloService.listarPorEscola(escolaUuid)
                .stream()
                .map(ModuloResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiReturn.of(modulos));
    }

    @PostMapping
    @EurekaApiOperation(
            summary = "Ativar módulos",
            description = "Ativa os módulos informados, cada um com sua data de expiração, e remove os não enviados."
    )
    public ResponseEntity<ApiReturn<List<ModuloResponse>>> ativarModulo(
            @RequestBody(description = "Dados para ativação de módulos", required = true)
            AtivarModuloRequest request) {
        List<ModuloResponse> modulos = escolaModuloService.ativarModulo(request)
                .stream()
                .map(ModuloResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiReturn.of(modulos));
    }
}
