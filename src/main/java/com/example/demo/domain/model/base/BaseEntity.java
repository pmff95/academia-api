package com.example.demo.domain.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.domain.enums.Status;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Column(
            name = "uuid",
            nullable = false,
            unique = true,
            insertable = false,
            updatable = false,
            columnDefinition = "UUID DEFAULT uuid_generate_v4()"
    )
    @Generated(GenerationTime.INSERT)
    protected UUID uuid;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    protected LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    @UpdateTimestamp
    protected LocalDateTime atualizadoEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected Status status = Status.ATIVO;
}
