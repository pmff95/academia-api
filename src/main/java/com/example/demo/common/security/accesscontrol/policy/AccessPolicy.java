package com.example.demo.common.security.accesscontrol.policy;

import com.example.demo.common.security.UsuarioLogado;

public interface AccessPolicy {

    /**
     * Retorna o nome da entidade à qual essa política se aplica.
     */
    String getEntityName();

    /**
     * Verifica se o usuário tem acesso ao recurso identificado.
     *
     * @param user        usuário autenticado
     * @param httpMethod  método HTTP da requisição
     * @param isStatusUpdate indica se é atualização de status
     * @param resourceId  identificador do recurso
     * @return true se o usuário tiver acesso; false caso contrário
     */
    boolean hasAccess(UsuarioLogado user, String httpMethod, boolean isStatusUpdate, Object resourceId);
}
