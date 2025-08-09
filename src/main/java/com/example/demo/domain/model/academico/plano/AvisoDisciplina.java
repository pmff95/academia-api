package com.example.demo.domain.model.academico.plano;

import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "aviso_disciplina")
@Audited
@Getter
@Setter
public class AvisoDisciplina extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "data")
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_disciplina_id", nullable = false)
    private PlanoDisciplina disciplina;
}
