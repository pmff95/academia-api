package com.example.demo.domain.model.academico.plano.chamada;

import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "registro_chamada")
@Audited
@Getter
@Setter
public class RegistroChamada extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_disciplina_id", nullable = false)
    private PlanoDisciplina planoDisciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Column(name = "data_aula", nullable = false)
    private LocalDate dataAula;

    @Column(name = "aula_dupla", nullable = false)
    private Boolean aulaDupla = false;

    @OneToMany(mappedBy = "registroChamada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemChamada> presencas;

    @Column(name = "preenchido", nullable = false)
    private Boolean preenchido = false;
}
