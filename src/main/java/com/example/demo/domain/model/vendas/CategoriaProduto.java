package com.example.demo.domain.model.vendas;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


@Entity
@Table(name = "categoria_produto")
@Audited
@Getter
@Setter
public class CategoriaProduto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Version
    private int version;
}
