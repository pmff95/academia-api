package com.example.demo.repository.specification;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.Turno;
import com.example.demo.domain.model.academico.Turma;
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

@ParameterObject
public class TurmaSpecification implements Specification<Turma> {

    @Schema(description = "Nome da turma", example = "1A")
    private final String nome;

    @Schema(description = "Série da turma")
    private final Serie serie;

    @Schema(description = "Turno")
    private final Turno turno;

    @Schema(description = "Status da turma")
    private final Status status;

    public TurmaSpecification(String nome, Serie serie, Turno turno, Status status) {
        this.nome = nome;
        this.serie = serie;
        this.turno = turno;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<Turma> root, CriteriaQuery<?> query, CriteriaBuilder criteria) {
        List<Predicate> predicates = new ArrayList<>();


        if (Objects.nonNull(nome) && !nome.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (Objects.nonNull(serie)) {
            predicates.add(criteria.equal(root.get("serie"), serie));
        }

        if (Objects.nonNull(turno)) {
            predicates.add(criteria.equal(root.get("turno"), turno));
        }

        if (Objects.nonNull(status)) {
            predicates.add(criteria.equal(root.get("status"), status));
        } else {
            predicates.add(criteria.notEqual(root.get("status"), Status.INATIVO));
        }

        return criteria.and(predicates.toArray(new Predicate[0]));
    }

    public String getNome() {
        return nome;
    }

    public Serie getSerie() {
        return serie;
    }

    public Turno getTurno() {
        return turno;
    }

    public Status getStatus() {
        return status;
    }
}
