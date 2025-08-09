package com.example.demo.domain.model.vendas;

import com.example.demo.domain.enums.Departamento;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Audited
@Getter
@Setter
public class Produto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "foto")
    private String foto;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(name = "departamento", nullable = false)
    private Departamento departamento;

    @Column(name = "quantidade_vendida", nullable = false)
    private Long quantidadeVendida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaProduto categoria;



    @Version
    private int version;

    public Produto() {

    }
}
