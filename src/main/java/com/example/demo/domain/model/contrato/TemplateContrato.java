package com.example.demo.domain.model.contrato;

import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "template_contrato")
@Audited
@Getter
@Setter
public class TemplateContrato extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "storage_key", nullable = false)
    private String key;

    @Column(name = "placeholders", nullable = false)
    private String placeholders;

    @Version
    private int version;
}
