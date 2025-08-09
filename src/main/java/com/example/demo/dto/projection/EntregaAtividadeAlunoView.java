package com.example.demo.dto.projection;

import com.example.demo.dto.projection.aluno.AlunoSummary;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EntregaAtividadeAlunoView {
    UUID getUuid();

    AlunoSummary getAluno();

    LocalDateTime getEntregueEm();

    String getArquivoUrl();

    Double getNota();

    @Value("#{target.entregueEm != null}")
    Boolean getEntregue();
}
