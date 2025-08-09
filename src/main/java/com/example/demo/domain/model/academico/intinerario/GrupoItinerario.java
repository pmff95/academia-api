package com.example.demo.domain.model.academico.intinerario;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Table(name = "grupo_itinerario")
@Audited
@Getter
@Setter
public class GrupoItinerario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "ano_letivo")
    private Integer anoLetivo;

    @ManyToOne
    @JoinColumn(name = "itinerario_id")
    private ItinerarioFormativo itinerario;

    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AlunoGrupoItinerario> alunos;

    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DisciplinaGrupoItinerario> disciplinas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;
}

