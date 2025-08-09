package com.example.demo.domain.model.controle;

import com.example.demo.domain.enums.TipoMatricula;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControleMatriculaId implements Serializable {

    @Column(nullable = false)
    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoMatricula tipo;

    @Column(name = "escola_id", nullable = false)
    private Long escolaId;
}
