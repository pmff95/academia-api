package com.example.demo.controller.academico.plano;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.dto.academico.DisciplinaCreateRequest;
import com.example.demo.service.common.PdfGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/academico/plano/resultado/boletim")
@Tag(name = "Boletim", description = "Endpoints para gerenciamento de boletim")
@RequiredArgsConstructor
public class BoletimController {
    private final PdfGeneratorService service;

    @PostMapping
    @EurekaApiOperation(
            summary = "Mock de boletim",
            description = "Gera um mock de boletim."
    )
    public ResponseEntity<byte[]> create() throws IOException {
        byte[] pdf = service.gerarPdfBoletimMock();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=boletim.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
