package com.example.demo.domain.model.controle;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "controle_matricula")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControleMatricula extends BaseEntity {

    @EmbeddedId
    private ControleMatriculaId id;

    @Column(name = "numero_atual", nullable = false)
    private Integer numeroAtual;
}
