package com.example.demo.dto.academico.plano;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public record EntregaAtividadeRequest(
        @Schema(description = "Arquivo da entrega da atividade")
        MultipartFile arquivo
) {
}
