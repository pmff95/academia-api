package com.example.demo.domain.model.academico.intinerario;

import com.example.demo.domain.model.academico.AulaHorario;
import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Table(name = "disciplina_grupo_itinerario")
@Audited
@Getter
@Setter
public class DisciplinaGrupoItinerario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private GrupoItinerario grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria = 0;
    @Column(name = "aulas_semana", nullable = false)
    private Integer aulasSemana = 0;

    @OneToMany(mappedBy = "disciplinaGrupoItinerario", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AulaHorario> horarios;
}

