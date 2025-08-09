package com.example.demo.common.security.accesscontrol.policy;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.service.professor.ProfessorService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfessorAccessPolicy implements AccessPolicy {

    private final ProfessorService professorService;

    public ProfessorAccessPolicy(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @Override
    public String getEntityName() {
        return EntityNames.PROFESSOR;
    }

    @Override
    public boolean hasAccess(UsuarioLogado currentUser, String httpMethod, boolean isStatusUpdate, Object resourceId) {
        UUID targetUuid = parseResourceId(resourceId);
        Professor professor = this.professorService.findByUuid(targetUuid);

        if (professor == null) {
            throw EurekaException.ofNotFound("Professor (" + targetUuid + ") não encontrado.");
        }

        UUID escolaUuid = currentUser.getEscola() != null ? currentUser.getEscola().getUuid() : null;
        boolean mesmaEscola = escolaUuid != null &&
                professor.getEscola() != null &&
                escolaUuid.equals(professor.getEscola().getUuid());

        boolean mesmoUsuario = professor.getUuid().equals(currentUser.getUuid());

        // Ativação/Inativação
        if ("PUT".equalsIgnoreCase(httpMethod) && isStatusUpdate) {
            return (currentUser.possuiPerfil(Perfil.MASTER) || currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) && mesmaEscola;
        }

        // Atualizações gerais
        if ("PUT".equalsIgnoreCase(httpMethod)) {
            if ((currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) && mesmaEscola) {
                return true;
            }

            if (currentUser.possuiPerfil(Perfil.PROFESSOR) && mesmoUsuario) {
                return true;
            }
        }

        // Leitura
        if ("GET".equalsIgnoreCase(httpMethod)) {
            return mesmaEscola || currentUser.possuiPerfil(Perfil.MASTER);
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
