package com.example.demo.dto;

import com.example.demo.domain.enums.StatusSolicitacao;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SolicitacaoDTO {
    private UUID uuid;
    private UUID professorUuid;
    private UUID alunoUuid;
    private String alunoNome;
    private LocalDateTime dataCadastro;
    private StatusSolicitacao status;
}
