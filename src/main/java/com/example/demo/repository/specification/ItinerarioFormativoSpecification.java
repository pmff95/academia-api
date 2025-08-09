package com.example.demo.repository.specification;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.intinerario.ItinerarioFormativo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ParameterObject
public class ItinerarioFormativoSpecification implements Specification<ItinerarioFormativo> {

    @Schema(description = "UUID da Escola", example = "00000000-0000-0000-0000-000000000000")
    private UUID escolaId;

    @Schema(description = "Nome do itinerário", example = "Saúde e Bem-estar")
    private final String nome;

    @Schema(description = "Status", example = "ATIVO")
    private final Status status;

    public ItinerarioFormativoSpecification(String nome, Status status) {
        this.nome = nome;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<ItinerarioFormativo> root, CriteriaQuery<?> query, CriteriaBuilder criteria) {
        List<Predicate> predicates = new ArrayList<>();

        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        if (currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) {
            this.escolaId = currentUser.getEscola().getUuid();
        }

        if (Objects.nonNull(escolaId)) {
            predicates.add(criteria.equal(root.get("escola").get("uuid"), this.escolaId));
        }

        if (Objects.nonNull(nome) && !nome.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (Objects.nonNull(status)) {
            predicates.add(criteria.equal(root.get("status"), status));
        } else {
            predicates.add(criteria.notEqual(root.get("status"), Status.INATIVO));
        }

        return criteria.and(predicates.toArray(new Predicate[0]));
    }

    public UUID getEscolaId() {
        return escolaId;
    }

    public String getNome() {
        return nome;
    }

    public Status getStatus() {
        return status;
    }
}
