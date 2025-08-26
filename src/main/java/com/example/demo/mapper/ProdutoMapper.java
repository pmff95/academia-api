package com.example.demo.mapper;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.dto.ProdutoDetalheDTO;
import com.example.demo.entity.Produto;
import com.example.demo.entity.ProdutoDetalhe;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoMapper {
    private final ModelMapper mapper;

    public ProdutoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProdutoDTO toDto(Produto entity) {
        ProdutoDTO dto = mapper.map(entity, ProdutoDTO.class);
        dto.setFornecedorUuid(entity.getFornecedor().getUuid());
        if (entity.getDetalhe() != null) {
            List<ProdutoDetalheDTO> detalhes = entity.getDetalhe().stream()
                    .map(d -> mapper.map(d, ProdutoDetalheDTO.class))
                    .collect(Collectors.toList());
            dto.setDetalhe(Collections.singletonList(detalhes));
        }
        return dto;
    }

    public Produto toEntity(ProdutoDTO dto) {
        Produto entity = mapper.map(dto, Produto.class);
        if (dto.getDetalhe() != null) {
            List<ProdutoDetalhe> detalhes = dto.getDetalhe().stream()
                    .flatMap(List::stream)
                    .map(d -> mapper.map(d, ProdutoDetalhe.class))
                    .collect(Collectors.toList());
            entity.setDetalhe(detalhes);
        }
        return entity;
    }
}

