package com.example.demo.service.usuario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.repository.usuario.UsuarioRepository;
import com.example.demo.service.common.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioImageService {

    private final StorageService storageService;
    private final UsuarioRepository usuarioRepository;

    public StorageOutput alterarImagemUsuario(UUID uuid, MultipartFile file) {
        StorageOutput output = storageService.uploadFile(file);
        Usuario usuario = usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Usuário não encontrado."));
        usuario.setFoto(output != null ? output.getUrl() : null);

        try {
            usuarioRepository.save(usuario);
        } catch (RuntimeException e) {
            if (output != null) {
                storageService.deleteFile(output.getKey());
            }
            throw e;
        }

        return output;
    }

    public void removerImagemUsuario(UUID uuid) {
        Usuario usuario = usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Usuário não encontrado."));

        String fotoUrl = usuario.getFoto();
        if (fotoUrl != null && !fotoUrl.isBlank()) {
            int lastSlash = fotoUrl.lastIndexOf('/');
            String key = lastSlash >= 0 ? fotoUrl.substring(lastSlash + 1) : fotoUrl;
            storageService.deleteFile(key);
        }

        usuario.setFoto(null);
        usuarioRepository.save(usuario);
    }
}
