package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Academia {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String codigo;

    private String uf;
    private String cidade;
    private String cep;
    private String numero;
    private String logradouro;
    private String bairro;
    private String telefone;

    @Column(nullable = false)
    private boolean ativo = true;

    private Integer limiteAlunos;

    @Column(nullable = false)
    private boolean exibirPatrocinadores = true;

    @Column(nullable = false)
    private boolean exibirMarketplace = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_uuid", referencedColumnName = "uuid", nullable = false)
    private Usuario admin;

    public void setCodigo(String codigo) {
        this.codigo = codigo != null ? codigo.toUpperCase() : null;
    }

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
