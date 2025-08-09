package com.example.demo.domain.model.saude;

import com.example.demo.domain.enums.GrauAlergia;
import com.example.demo.domain.enums.TipoAlergia;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "alergia")
@Audited
@Getter
@Setter
public class Alergia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAlergia tipo;

    @Column(name = "substancia", nullable = false)
    private String substancia;

    @Enumerated(EnumType.STRING)
    @Column(name = "gravidade", nullable = false)
    private GrauAlergia gravidade;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "data_diagnostico")
    private LocalDate dataDiagnostico;

    @Column(name = "cuidados_emergenciais")
    private Boolean cuidadosEmergenciais = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Version
    private int version;
}
