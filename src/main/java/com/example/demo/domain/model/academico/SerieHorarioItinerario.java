package com.example.demo.domain.model.academico;

import com.example.demo.domain.enums.DiaSemana;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "serie_horario_itinerario",
       uniqueConstraints = @UniqueConstraint(columnNames = {"turma_id", "dia", "ordem"}))
@Audited
@Getter
@Setter
public class SerieHorarioItinerario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;

    @Column(nullable = false)
    private Integer ordem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;
}
