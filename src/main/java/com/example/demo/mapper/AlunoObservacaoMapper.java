package com.example.demo.mapper;

import com.example.demo.dto.AlunoObservacaoDTO;
import com.example.demo.entity.AlunoObservacao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AlunoObservacaoMapper {
    private final ModelMapper mapper;

    public AlunoObservacaoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AlunoObservacaoDTO toDto(AlunoObservacao entity) {
        return mapper.map(entity, AlunoObservacaoDTO.class);
    }

    public AlunoObservacao toEntity(AlunoObservacaoDTO dto) {
        return mapper.map(dto, AlunoObservacao.class);
    }
}
