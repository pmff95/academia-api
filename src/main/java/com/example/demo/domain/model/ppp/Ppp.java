package com.example.demo.domain.model.ppp;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "ppp")
@Audited
@Getter
@Setter
public class Ppp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "inicio_vigencia", nullable = false)
    private LocalDate inicioVigencia;

    @Column(name = "fim_vigencia", nullable = false)
    private LocalDate fimVigencia;

    @Column(name = "data_aprovacao")
    private LocalDate dataAprovacao;

    @Column(name = "responsavel")
    private String responsavel;

    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    @Version
    private int version;
}
