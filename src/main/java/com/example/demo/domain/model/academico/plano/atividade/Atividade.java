package com.example.demo.domain.model.academico.plano.atividade;

import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "atividade")
@Audited
@Getter
@Setter
public class Atividade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "prazo")
    private LocalDate prazo;

    @Column(name = "arquivo_url")
    private String arquivoUrl;

    @Column(name = "is_grupo")
    private boolean isGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_disciplina_id", nullable = false)
    private PlanoDisciplina planoDisciplina;

    @OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntregaAtividade> entregaAtividades;
}
