package com.example.demo.domain.model.instituicao;

import com.example.demo.domain.converter.CnpjConverter;
import com.example.demo.domain.enums.TipoPeriodo;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
@Table(name = "escola")
@Getter
@Setter
public class Escola extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nome_curto", nullable = false)
    private String nomeCurto;

    @Convert(converter = CnpjConverter.class)
    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "tipo_periodo")
    @Enumerated(value = EnumType.STRING)
    private TipoPeriodo tipoPeriodo;


    @OneToMany(mappedBy = "escola", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EscolaModulo> modulos;


    @Version
    private int version;

    public Escola() {
    }

    public Escola(Long id) {
        this.id = id;
    }
}
