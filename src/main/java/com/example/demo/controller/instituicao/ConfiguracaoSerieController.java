package com.example.demo.controller.instituicao;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.instituicao.ConfiguracaoSerieItemRequest;
import com.example.demo.dto.instituicao.ConfiguracaoSerieResponse;
import com.example.demo.service.instituicao.ConfiguracaoSerieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuracao-serie")
@Tag(name = "Configuração Série x Tipo de Período")
@RequiredArgsConstructor
public class ConfiguracaoSerieController {

    private final ConfiguracaoSerieService service;

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','MASTER')")
    @EurekaApiOperation(summary = "Salvar ou atualizar configurações de série e tipo de período")
    public ResponseEntity<ApiReturn<String>> salvarOuAtualizar(
            @RequestBody @Valid List<ConfiguracaoSerieItemRequest> request) {
        String msg = service.salvarOuAtualizar(request);
        return ResponseEntity.ok(ApiReturn.of(msg));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MASTER')")
    @EurekaApiOperation(summary = "Listar configurações de série e tipo de período")
    public ResponseEntity<ApiReturn<List<ConfiguracaoSerieResponse>>> listar() {
        return ResponseEntity.ok(ApiReturn.of(service.listar()));

    }
}
