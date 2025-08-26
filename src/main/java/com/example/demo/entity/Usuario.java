package com.example.demo.entity;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Tema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.demo.entity.Academia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public class Usuario {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    private LocalDate dataNascimento;

    private String telefone;

    private String telefoneSecundario;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nick;

    private String numero;

    private String cep;

    private String logradouro;

    private String uf;

    private String cidade;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tema tema = Tema.CLARO;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private boolean primeiroAcesso = true;

    @ManyToOne
    @JoinColumn(name = "academia_uuid")
    private Academia academia;

    private LocalDateTime ultimoAcesso;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
