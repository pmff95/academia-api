package com.example.demo.domain.model.academico.plano.avaliacao;

import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "avaliacao")
@Audited
@Getter
@Setter
public class Avaliacao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "prazo_lancamento_notas")
    private LocalDate prazoLancamentoNotas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_disciplina_id", nullable = false)
    private PlanoDisciplina disciplina;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas;
}