package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Academia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String uf;
    private String cidade;
    private String numero;
    private String logradouro;
    private String bairro;
    private String telefone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_uuid", referencedColumnName = "uuid", nullable = false)
    private Usuario admin;
}
