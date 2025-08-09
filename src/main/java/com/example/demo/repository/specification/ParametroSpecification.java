package com.example.demo.repository.specification;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.domain.enums.TipoValor;
import com.example.demo.domain.model.parametro.Parametro;
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
public class ParametroSpecification implements Specification<Parametro> {

    @Schema(description = "Chave do parâmetro", example = "limite_maximo")
    private final String chave;

    @Schema(description = "Descrição do parâmetro", example = "Limite máximo de alunos")
    private final String descricao;

    @Schema(description = "Tipo do parâmetro")
    private final TipoParametro tipo;

    @Schema(description = "Tipo do valor")
    private final TipoValor tipoValor;

    @Schema(description = "Necessário confirmação", example = "true")
    private final Boolean necessarioConfirmacao;

    @Schema(description = "Status do parâmetro", example = "ATIVO")
    private final Status status;

    public ParametroSpecification(String chave, String descricao, TipoParametro tipo,
                                  TipoValor tipoValor, Boolean necessarioConfirmacao,
                                  Status status) {
        this.chave = chave;
        this.descricao = descricao;
        this.tipo = tipo;
        this.tipoValor = tipoValor;
        this.necessarioConfirmacao = necessarioConfirmacao;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<Parametro> root, CriteriaQuery<?> query, CriteriaBuilder criteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(chave) && !chave.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("chave")), "%" + chave.toLowerCase() + "%"));
        }

        if (Objects.nonNull(descricao) && !descricao.isEmpty()) {
            predicates.add(criteria.like(criteria.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
        }

        if (Objects.nonNull(tipo)) {
            predicates.add(criteria.equal(root.get("tipo"), tipo));
        }

        if (Objects.nonNull(tipoValor)) {
            predicates.add(criteria.equal(root.get("tipoValor"), tipoValor));
        }

        if (Objects.nonNull(necessarioConfirmacao)) {
            predicates.add(criteria.equal(root.get("necessarioConfirmacao"), necessarioConfirmacao));
        }

        if (Objects.nonNull(status)) {
            predicates.add(criteria.equal(root.get("status"), status));
        } else {
            predicates.add(criteria.notEqual(root.get("status"), Status.INATIVO));
        }

        return criteria.and(predicates.toArray(new Predicate[0]));
    }

    public String getChave() { return chave; }
    public String getDescricao() { return descricao; }
    public TipoParametro getTipo() { return tipo; }
    public TipoValor getTipoValor() { return tipoValor; }
    public Boolean getNecessarioConfirmacao() { return necessarioConfirmacao; }
    public Status getStatus() { return status; }
}
