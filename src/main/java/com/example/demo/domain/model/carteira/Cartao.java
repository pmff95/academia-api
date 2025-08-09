package com.example.demo.domain.model.carteira;

import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


@Entity
@Table(name = "cartao_carteira")
@Audited
@Getter
@Setter
public class Cartao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "senha", nullable = false)
    private String senha;


    @Version
    private int version;

    public Cartao() {

    }
}
