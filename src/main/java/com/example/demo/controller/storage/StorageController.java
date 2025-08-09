package com.example.demo.controller.storage;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.service.common.StorageService;
import com.example.demo.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Storage", description = "Endpoints para gerenciamento de arquivos")
public class StorageController {

    private final StorageService storageService;
    private final UsuarioService usuarioService;

    public StorageController(StorageService storageService, UsuarioService usuarioService) {
        this.storageService = storageService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @EurekaApiOperation(
            summary = "Upload de arquivo",
            description = "Realiza o upload de um arquivo para o serviço de armazenamento."
    )
    public ResponseEntity<?> upload(
            @Parameter(description = "Arquivo a ser enviado", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            StorageOutput output = storageService.uploadFile(file);

            return ResponseEntity.ok().body(Map.of(
                    "fileName", output.getKey(),
                    "url", output.getUrl()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @PostMapping("/usuarios/{uuid}")
    @EurekaApiOperation(
            summary = "Alterar imagem do usuário",
            description = "Faz upload e altera a imagem do usuário informado."
    )
    public ResponseEntity<?> alterarImagemUsuario(
            @Parameter(description = "UUID do usuário", required = true)
            @PathVariable("uuid") UUID uuid,
            @Parameter(description = "Arquivo da imagem", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            StorageOutput output = usuarioService.alterarImagemUsuario(uuid, file);

            return ResponseEntity.ok().body(Map.of(
                    "fileName", output != null ? output.getKey() : null,
                    "url", output != null ? output.getUrl() : null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }
}
