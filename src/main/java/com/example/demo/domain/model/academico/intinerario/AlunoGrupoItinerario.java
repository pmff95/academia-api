package com.example.demo.domain.model.academico.intinerario;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "aluno_grupo_itinerario")
@Audited
@Getter
@Setter
public class AlunoGrupoItinerario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private GrupoItinerario grupo;
}

