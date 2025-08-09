package com.example.demo.mapper;

import com.example.demo.dto.MedidaHistoricaDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.MedidaHistorica;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MedidaHistoricaMapper {
    private final ModelMapper mapper;

    public MedidaHistoricaMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public MedidaHistoricaDTO toDto(MedidaHistorica entity) {
        MedidaHistoricaDTO dto = mapper.map(entity, MedidaHistoricaDTO.class);
        dto.setAlunoId(entity.getAluno().getId());
        return dto;
    }

    public MedidaHistorica toEntity(MedidaHistoricaDTO dto, Aluno aluno) {
        MedidaHistorica entity = mapper.map(dto, MedidaHistorica.class);
        entity.setAluno(aluno);
        return entity;
    }
}
