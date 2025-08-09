package com.example.demo.domain.model.academico;

import com.example.demo.domain.enums.DiaSemana;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalTime;

@Entity
@Table(name = "aula_horario_grade")
@Audited
@Getter
@Setter
public class AulaHorarioGrade extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia")
    private DiaSemana dia;

    @Column(name = "inicio")
    private LocalTime inicio;
    @Column(name = "fim")
    private LocalTime fim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_itinerario_id")
    private com.example.demo.domain.model.academico.intinerario.GrupoItinerario grupo;

}
