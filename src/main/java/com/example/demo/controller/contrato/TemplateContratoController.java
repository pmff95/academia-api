package com.example.demo.controller.contrato;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.service.contrato.TemplateContratoService;
import com.example.demo.dto.contrato.TemplateResumoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contratos/templates")
@Tag(name = "Templates de Contrato")
public class TemplateContratoController {

    private final TemplateContratoService service;

    public TemplateContratoController(TemplateContratoService service) {
        this.service = service;
    }

    @PostMapping
    @EurekaApiOperation(summary = "Upload de template", description = "Envia um template de contrato em HTML")
    public ResponseEntity<ApiReturn<String>> upload(@RequestParam("file") MultipartFile file, @RequestParam("nome") String nome) throws IOException {
        service.salvarTemplate(file, nome);
        return ResponseEntity.ok(ApiReturn.of("Contrato salvo com sucesso"));
    }

    @GetMapping
    @EurekaApiOperation(summary = "Listar templates", description = "Lista templates de contrato")
    public ResponseEntity<ApiReturn<Page<TemplateResumoDTO>>> list(
            @RequestParam(value = "nome", required = false) String nome,
            @ParameterObject @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(nome, pageable)));
    }

    @PostMapping("/{uuid}/gerar")
    @EurekaApiOperation(summary = "Gerar contrato", description = "Gera PDF a partir do template e dados enviados")
    public ResponseEntity<byte[]> gerar(@PathVariable("uuid") UUID uuid, @RequestBody Map<String, Object> dados) throws IOException {
        byte[] pdf = service.gerarPdf(uuid, dados);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contrato.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
