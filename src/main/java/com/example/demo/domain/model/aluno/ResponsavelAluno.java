package com.example.demo.domain.model.aluno;

import com.example.demo.domain.enums.GrauParentesco;
import com.example.demo.domain.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "responsavel_aluno")
@Audited
@Getter
@Setter
public class ResponsavelAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Usuario responsavel;

    @Column(name = "grau_parentesco", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private GrauParentesco grauParentesco;

    @Version
    private int version;

    public ResponsavelAluno() {
    }
}
