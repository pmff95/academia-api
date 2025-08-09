package com.example.demo.domain.model.instituicao;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "periodo_avaliativo")
@Audited
@Getter
@Setter
public class PeriodoAvaliativo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Enumerated(EnumType.STRING)
    @Column(name = "serie", nullable = false)
    private Serie serie;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "fim")
    private LocalDate fim;

    @Column(name = "gerado_automaticamente", nullable = false)
    private Boolean geradoAutomaticamente = true;
}