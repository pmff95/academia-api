package com.example.demo.common.audit;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.base.RevisionInfo;
import org.hibernate.envers.RevisionListener;

public class RevisionInfoListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        RevisionInfo rev = (RevisionInfo) revisionEntity;

        try {
            UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
            rev.setNome(usuarioLogado.getName());
            rev.setUsuarioId(usuarioLogado.getId());
            if (usuarioLogado.getEscola() != null) {
                rev.setEscolaId(usuarioLogado.getEscola().getId());
            }
        } catch (Exception e) {
            rev.setNome("System");
        }


    }

}
