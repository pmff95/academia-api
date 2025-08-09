package com.example.demo.domain.model.professor;

import com.example.demo.domain.enums.DiaSemana;
import com.example.demo.domain.enums.Turno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "horario_disponivel")
@Audited
@Getter
@Setter
public class HorarioDisponivel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana dia;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false)
    private Turno turno;

    @Column(name = "horario_inicio", nullable = false)
    private LocalTime inicio;

    @Column(name = "horario_fim", nullable = false)
    private LocalTime fim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    

    

    
}
