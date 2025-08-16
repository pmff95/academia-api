package com.example.demo.mapper;

import com.example.demo.dto.FichaTreinoCategoriaDTO;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoHistoricoDTO;
import com.example.demo.dto.FichaTreinoExercicioDTO;
import com.example.demo.entity.FichaTreino;
import com.example.demo.entity.FichaTreinoCategoria;
import com.example.demo.entity.FichaTreinoExercicio;
import com.example.demo.entity.FichaTreinoHistorico;
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
        if (ficha.getAluno() != null) {
            dto.setAlunoUuid(ficha.getAluno().getUuid());
        }
        if (ficha.getProfessor() != null) {
            dto.setProfessorUuid(ficha.getProfessor().getUuid());
        }
        dto.setNome(ficha.getNome());
        dto.setPreset(ficha.isPreset());
        dto.setDataCadastro(ficha.getDataCadastro());
        dto.setDataAtualizacao(ficha.getDataAtualizacao());
        dto.setCategorias(ficha.getCategorias().stream().map(this::toCategoriaDto).collect(Collectors.toList()));
        return dto;
    }

    public FichaTreino toEntity(FichaTreinoDTO dto) {
        return mapper.map(dto, FichaTreino.class);
    }

    private FichaTreinoCategoriaDTO toCategoriaDto(FichaTreinoCategoria categoria) {
        FichaTreinoCategoriaDTO dto = new FichaTreinoCategoriaDTO();
        dto.setUuid(categoria.getUuid());
        dto.setNome(categoria.getNome());
        dto.setExercicios(categoria.getExercicios().stream().map(this::toExercicioDto).collect(Collectors.toList()));
        return dto;
    }

    private FichaTreinoExercicioDTO toExercicioDto(FichaTreinoExercicio exercicio) {
        FichaTreinoExercicioDTO dto = new FichaTreinoExercicioDTO();
        dto.setExercicioUuid(exercicio.getExercicio().getUuid());
        dto.setExercicioNome(exercicio.getExercicio().getNome());
        dto.setRepeticoes(exercicio.getRepeticoes());
        dto.setCarga(exercicio.getCarga());
        return dto;
    }

    public FichaTreinoHistoricoDTO toHistoricoDto(FichaTreinoHistorico historico) {
        FichaTreinoHistoricoDTO dto = new FichaTreinoHistoricoDTO();
        FichaTreino ficha = historico.getFicha();
        dto.setUuid(ficha.getUuid());
        dto.setNome(ficha.getNome());
        dto.setDataCadastro(ficha.getDataCadastro());
        dto.setDataAtualizacao(ficha.getDataAtualizacao());
        if (ficha.getProfessor() != null) {
            dto.setProfessorUuid(ficha.getProfessor().getUuid());
            dto.setProfessorNome(ficha.getProfessor().getNome());
        }
        dto.setAtual(historico.isAtual());
        return dto;
    }
}
