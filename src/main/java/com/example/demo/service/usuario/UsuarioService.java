package com.example.demo.service.usuario;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.dto.projection.usuario.UsuarioFull;
import com.example.demo.dto.projection.usuario.UsuarioSummary;
import com.example.demo.dto.usuario.CurrentUserView;
import com.example.demo.dto.usuario.TrocarSenhaRequest;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.repository.specification.UsuarioSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioCrudService crudService;
    private final UsuarioEmailService emailService;
    private final UsuarioImageService imageService;

    public UUID createUser(UsuarioRequest request) {
        return crudService.createUser(request);
    }

    public void registrarUltimoAcesso(String matricula) {
        crudService.registrarUltimoAcesso(matricula);
    }

    public void updateUser(UUID uuid, UsuarioRequest request) {
        crudService.updateUser(uuid, request);
    }

    public Usuario findByUuid(UUID uuid) {
        return crudService.findByUuid(uuid);
    }

    public void changeUserStatus(UUID uuid, Status status) {
        crudService.changeUserStatus(uuid, status);
    }

    public Usuario findByMatriculaComEscola(String matricula) {
        return crudService.findByMatriculaComEscola(matricula);
    }

    public Page<UsuarioSummary> findAll(UsuarioSpecification specification, Pageable pageable) {
        return crudService.findAll(specification, pageable);
    }

    public <T> T findByUuid(UUID uuid, Class<T> clazz) {
        return crudService.findByUuid(uuid, clazz);
    }

    public Optional<UsuarioFull> findByEscolaIdAndPerfil(UUID escolaId, Perfil perfil) {
        return crudService.findByEscolaIdAndPerfil(escolaId, perfil);
    }

    public CurrentUserView findCurrent() {
        return crudService.findCurrent();
    }

    public void save(Usuario user) {
        crudService.save(user);
    }

    public void trocarResponsavelDaEscola(UUID uuid) {
        crudService.trocarResponsavelDaEscola(uuid);
    }

    public void enviarEmailRecuperacaoSenha(String nome, String email, String senha) {
        emailService.enviarEmailRecuperacaoSenha(nome, email, senha);
    }

    public StorageOutput alterarImagemUsuario(UUID uuid, MultipartFile file) {
        return imageService.alterarImagemUsuario(uuid, file);
    }

    public void removerImagemUsuario(UUID uuid) {
        imageService.removerImagemUsuario(uuid);
    }
}
