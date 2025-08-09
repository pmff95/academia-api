package com.example.demo.repository.specification;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.aluno.Aluno;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ParameterObject
public class AlunoSpecification implements Specification<Aluno> {

    @Schema(description = "UUID da Escola", example = "00000000-0000-0000-0000-000000000000")
    private UUID escolaId;

    @Schema(description = "Nome do aluno", example = "João")
    private final String nome;

    @Schema(description = "CPF do aluno", example = "123.456.789-00")
    private final String cpf;

    @Schema(description = "E-mail do aluno", example = "joao@example.com")
    private final String email;

    @Schema(description = "Status do aluno", example = "ATIVO")
    private final Status status;

    @Schema(description = "Número de matrícula do aluno", example = "2021001234")
    private final String matricula;

    @Schema(description = "UUID do responsável", example = "00000000-0000-0000-0000-000000000000")
    private final UUID responsavelId;

    @Schema(description = "Série do aluno")
    private final Serie serie;

    public AlunoSpecification(
            String nome, String cpf, String email, Status status,
            String matricula, UUID responsavelId,
            Serie serie) {

        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.status = status;
        this.matricula = matricula;
        this.responsavelId = responsavelId;
        this.serie = serie;
    }

    @Override
    public Predicate toPredicate(Root<Aluno> root, CriteriaQuery<?> query, CriteriaBuilder criteria) {

        List<Predicate> predicates = new ArrayList<>();

        // Se for ADMIN ou FUNCIONARIO, força o filtro para a escola do usuário logado
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        if (currentUser.possuiPerfil(Perfil.ADMIN) || currentUser.possuiPerfil(Perfil.FUNCIONARIO)) {
            this.escolaId = currentUser.getEscola().getUuid();
        }

        // Se houver valor para escolaId (seja vindo do parâmetro ou do usuário logado) adiciona o predicate de filtro
        if (Objects.nonNull(escolaId)) {
            predicates.add(criteria.equal(root.get("escola").get("uuid"), this.escolaId));
        }

        if (Objects.nonNull(nome) && !nome.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("nome")), "%" + this.nome.toLowerCase() + "%"));
        }

        if (Objects.nonNull(cpf) && !cpf.isEmpty()) {
            predicates.add(criteria.like(root.get("cpf"), "%"
                    + this.cpf.replaceAll("\\D", "") + "%"));
        }

        if (Objects.nonNull(email) && !email.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("email")), "%" + this.email.toLowerCase() + "%"));
        }

        // Garante que só exibira ALUNO
        predicates.add(criteria.equal(root.get("perfil"), Perfil.ALUNO));

        if (Objects.nonNull(status)) {
            predicates.add(criteria.equal(root.get("status"), this.status));
        } else {
            predicates.add(criteria.notEqual(root.get("status"), Status.INATIVO));
        }

        if (Objects.nonNull(matricula) && !matricula.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("matricula")), "%" + this.matricula.toLowerCase() + "%"));
        }

        if (Objects.nonNull(responsavelId)) {
            predicates.add(criteria.equal(
                    root.join("responsaveis").get("responsavel").get("uuid"),
                    this.responsavelId
            ));
        }

        if (Objects.nonNull(serie)) {
            query.distinct(true);
            Join<Object, Object> turmaJoin = root.join("turmas");
            predicates.add(criteria.equal(turmaJoin.get("serie"), serie));
        }

        return criteria.and(predicates.stream().toArray(Predicate[]::new));
    }

    public UUID getEscolaId() {
        return escolaId;
    }

    public UUID getResponsavelId() {
        return responsavelId;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public Status getStatus() {
        return status;
    }

    public Serie getSerie() {
        return serie;
    }

}
