package com.example.demo.dto.projection;

import com.example.demo.dto.projection.aluno.AlunoSummary;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaAtividadeAlunoViewImpl(
        UUID uuid,
        AlunoSummary aluno,
        LocalDateTime entregueEm,
        String arquivoUrl,
        Double nota,
        Boolean entregue
) implements EntregaAtividadeAlunoView {
    public EntregaAtividadeAlunoViewImpl(EntregaAtividadeAlunoView view) {
        this(view != null ? view.getUuid() : null,
                view != null ? view.getAluno() : null,
                view != null ? view.getEntregueEm() : null,
                view != null ? view.getArquivoUrl() : null,
                view != null ? view.getNota() : null,
                view != null && view.getEntregueEm() != null);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public AlunoSummary getAluno() {
        return aluno;
    }

    @Override
    public LocalDateTime getEntregueEm() {
        return entregueEm;
    }

    @Override
    public String getArquivoUrl() {
        return arquivoUrl;
    }

    @Override
    public Double getNota() {
        return nota;
    }

    @Override
    public Boolean getEntregue() {
        return entregue;
    }
}
