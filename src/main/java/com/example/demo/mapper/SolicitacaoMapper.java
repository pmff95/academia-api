package com.example.demo.mapper;

import com.example.demo.dto.SolicitacaoDTO;
import com.example.demo.entity.Solicitacao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoMapper {
    private final ModelMapper mapper;

    public SolicitacaoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public SolicitacaoDTO toDto(Solicitacao solicitacao) {
        SolicitacaoDTO dto = mapper.map(solicitacao, SolicitacaoDTO.class);
        dto.setProfessorUuid(solicitacao.getProfessor().getUuid());
        dto.setAlunoUuid(solicitacao.getAluno().getUuid());
        dto.setAlunoNome(solicitacao.getAluno().getNome());
        return dto;
    }
}
