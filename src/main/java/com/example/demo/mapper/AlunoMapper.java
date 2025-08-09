package com.example.demo.mapper;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.entity.Aluno;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {
    private final ModelMapper mapper;

    public AlunoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AlunoDTO toDto(Aluno aluno) {
        AlunoDTO dto = mapper.map(aluno, AlunoDTO.class);
        if (aluno.getProfessor() != null) {
            dto.setProfessorId(aluno.getProfessor().getId());
        }
        return dto;
    }

    public Aluno toEntity(AlunoDTO dto) {
        return mapper.map(dto, Aluno.class);
    }
}
