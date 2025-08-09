package com.example.demo.domain.model.instituicao;

import com.example.demo.domain.enums.NomeModulo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@Entity
@Audited
@Getter
@Setter
public class EscolaModulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("modulos")
    private Escola escola;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NomeModulo modulo;

    private boolean ativo;

    private LocalDate dataAtivacao;

    private LocalDate dataExpiracao;

    public boolean isExpirado() {
        return dataExpiracao != null && dataExpiracao.isBefore(LocalDate.now());
    }

    public boolean isValido() {
        return ativo && !isExpirado();
    }
}
