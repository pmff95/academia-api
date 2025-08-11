package com.example.demo.dto;

import com.example.demo.domain.enums.StatusSolicitacao;
import lombok.Data;

import java.util.UUID;

@Data
public class SolicitacaoDTO {
    private UUID uuid;
    private UUID professorUuid;
    private UUID alunoUuid;
    private StatusSolicitacao status;
}
