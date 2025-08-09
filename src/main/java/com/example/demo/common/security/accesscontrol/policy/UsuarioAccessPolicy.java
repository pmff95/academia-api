package com.example.demo.common.security.accesscontrol.policy;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.service.usuario.UsuarioService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioAccessPolicy implements AccessPolicy {

    private final UsuarioService usuarioService;

    public UsuarioAccessPolicy(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public String getEntityName() {
        return EntityNames.USUARIO;
    }

    @Override
    public boolean hasAccess(UsuarioLogado currentUser, String httpMethod, boolean isStatusUpdate, Object resourceId) {
        UUID targetUuid = parseResourceId(resourceId);
        Usuario userEntity = usuarioService.findByUuid(targetUuid);

        if (userEntity == null) {
            throw EurekaException.ofNotFound("Usuário (" + targetUuid + ") não encontrado.");
        }

        UUID escolaUuid = currentUser.getEscola() != null ? currentUser.getEscola().getUuid() : null;

        boolean mesmaEscola = escolaUuid != null &&
                userEntity.getEscola() != null &&
                escolaUuid.equals(userEntity.getEscola().getUuid());

        boolean mesmoUsuario = userEntity.getUuid().equals(currentUser.getUuid());

        // Regras específicas quando o usuário alvo é um ALUNO
        if (userEntity.getPerfil() == Perfil.ALUNO) {
            if ("GET".equalsIgnoreCase(httpMethod)) {
                if ((currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) && mesmaEscola) {
                    return true;
                }
                return mesmoUsuario;
            }
            return ("PUT".equalsIgnoreCase(httpMethod) || "DELETE".equalsIgnoreCase(httpMethod)) && mesmoUsuario;
        }

        // Se for ativação/inativação, apenas MASTER, ADMIN e FUNCIONÁRIO podem fazer isso
        if ("PUT".equals(httpMethod) && isStatusUpdate) {
            return (currentUser.possuiPerfil(Perfil.MASTER) || currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) && mesmaEscola;
        }

        // Se for uma atualização normal de dados ou remoção de imagem
        if ("PUT".equalsIgnoreCase(httpMethod) || "DELETE".equalsIgnoreCase(httpMethod)) {
            // ADMIN e FUNCIONÁRIO podem editar usuários da mesma escola
            if ((currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) && mesmaEscola) {
                return true;
            }

            // RESPONSÁVEL, PDV, PROFESSOR e RESPONSÁVEL CONTRATUAL só podem editar/remover seus próprios dados
            return (
                    currentUser.possuiPerfil(Perfil.RESPONSAVEL) ||
                    currentUser.possuiPerfil(Perfil.PDV) ||
                    currentUser.possuiPerfil(Perfil.PROFESSOR) ||
                    currentUser.possuiPerfil(Perfil.RESPONSAVEL_CONTRATUAL)
            ) && mesmoUsuario;
        }

        // Regras de leitura
        if ("GET".equalsIgnoreCase(httpMethod)) {
            if ((currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) && mesmaEscola) {
                return true;
            }

            return (
                    currentUser.possuiPerfil(Perfil.RESPONSAVEL) ||
                    currentUser.possuiPerfil(Perfil.PDV) ||
                    currentUser.possuiPerfil(Perfil.PROFESSOR) ||
                    currentUser.possuiPerfil(Perfil.RESPONSAVEL_CONTRATUAL) ||
                    currentUser.possuiPerfil(Perfil.ALUNO)
            ) && mesmoUsuario;
        }

        return false;
    }

    private UUID parseResourceId(Object resourceId) {
        try {
            return UUID.fromString(resourceId.toString());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ResourceId inválido: " + resourceId, e);
        }
    }
}

