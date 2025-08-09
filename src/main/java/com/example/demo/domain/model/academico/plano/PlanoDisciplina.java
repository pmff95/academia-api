package com.example.demo.domain.model.academico.plano;

import com.example.demo.domain.model.academico.TurmaDisciplina;
import com.example.demo.domain.model.academico.intinerario.DisciplinaGrupoItinerario;
import com.example.demo.domain.model.academico.plano.atividade.Atividade;
import com.example.demo.domain.model.academico.plano.avaliacao.Avaliacao;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Table(name = "plano_disciplina")
@Audited
@Getter
@Setter
public class PlanoDisciplina extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_disciplina_id")
    private TurmaDisciplina turmaDisciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_grupo_id")
    private DisciplinaGrupoItinerario disciplinaGrupo;

    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo;

    @OneToMany(mappedBy = "planoDisciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividades;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialEstudo> materiais;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumoAula> resumos;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvisoDisciplina> avisos;

    public boolean isItinerario() {
        return disciplinaGrupo != null;
    }
}
