package com.example.demo.common.security.accesscontrol.policy;

import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.domain.enums.Perfil;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Política de acesso para recursos de Aluno.
 */
@Component
public class AlunoAccessPolicy implements AccessPolicy {

    @Override
    public String getEntityName() {
        return EntityNames.ALUNO;
    }

    @Override
    public boolean hasAccess(UsuarioLogado currentUser, String httpMethod, boolean isStatusUpdate, Object resourceId) {
        if (currentUser == null) {
            return false;
        }

        // Usuário MASTER possui acesso irrestrito
        if (currentUser.possuiPerfil(Perfil.MASTER)) {
            return true;
        }

        // Apenas para leitura própria
        if ("GET".equalsIgnoreCase(httpMethod)) {
            try {
                UUID target = UUID.fromString(resourceId.toString());
                return currentUser.getUuid().equals(target);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        return false;
    }
}

