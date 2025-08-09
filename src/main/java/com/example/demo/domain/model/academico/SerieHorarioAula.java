package com.example.demo.domain.model.academico;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalTime;

@Entity
@Table(name = "serie_horario_aula",
       uniqueConstraints = @UniqueConstraint(columnNames = {"serie", "ordem"}))
@Audited
@Getter
@Setter
public class SerieHorarioAula extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Serie serie;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private LocalTime inicio;

    @Column(nullable = false)
    private LocalTime fim;

    @Column(nullable = false)
    private boolean intervalo = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;
}

