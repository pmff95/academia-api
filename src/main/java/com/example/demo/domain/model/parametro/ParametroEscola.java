package com.example.demo.domain.model.parametro;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "parametro_escola",
       uniqueConstraints = @UniqueConstraint(columnNames = {"escola_id", "parametro_id"}))
@Getter
@Setter
public class ParametroEscola extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parametro_id", nullable = false)
    private Parametro parametro;

    @Column(name = "valor_customizado")
    private String valorCustomizado;

    @Version
    private int version;
}
