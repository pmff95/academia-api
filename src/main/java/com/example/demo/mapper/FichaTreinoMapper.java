package com.example.demo.mapper;

import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.entity.Exercicio;
import com.example.demo.entity.FichaTreino;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FichaTreinoMapper {
    private final ModelMapper mapper;

    public FichaTreinoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public FichaTreinoDTO toDto(FichaTreino ficha) {
        FichaTreinoDTO dto = new FichaTreinoDTO();
        dto.setUuid(ficha.getUuid());
        dto.setAlunoUuid(ficha.getAluno().getUuid());
        if (ficha.getProfessor() != null) {
            dto.setProfessorUuid(ficha.getProfessor().getUuid());
        }
        dto.setCategoria(ficha.getCategoria());
        dto.setExerciciosUuids(ficha.getExercicios().stream().map(Exercicio::getUuid).collect(Collectors.toList()));
        return dto;
    }

    public FichaTreino toEntity(FichaTreinoDTO dto) {
        return mapper.map(dto, FichaTreino.class);
    }
}
