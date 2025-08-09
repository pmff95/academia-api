package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class MedidaHistorica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(optional = false)
    private Aluno aluno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Segmento segmento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMedida tipoMedida;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    private String unidade;

    @Column(nullable = false)
    private ZonedDateTime dataMedicao;

    private String observacao;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
