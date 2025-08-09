package com.example.demo.domain.model.instituicao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "escola_endereco")
@Audited
@Getter
@Setter
public class EscolaEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false, unique = true)
    private Escola escola;


    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "endereco", nullable = false, length = 100)
    private String endereco;

    @Column(name = "numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "bairro", nullable = false, length = 50)
    private String bairro;

    @Column(name = "complemento", nullable = false, length = 50)
    private String complemento;

    @Column(name = "cidade", nullable = false, length = 50)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Version
    private int version = 0;
}
