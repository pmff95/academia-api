package com.example.demo.mapper;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.dto.ProdutoDetalheDTO;
import com.example.demo.entity.Produto;
import com.example.demo.entity.ProdutoDetalhe;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

        if (entity.getFornecedor() != null) {
            dto.setFornecedorUuid(entity.getFornecedor().getUuid());
        }

        if (entity.getDetalhe() != null) {
            List<ProdutoDetalheDTO> detalhes = entity.getDetalhe().stream()
                    .map(d -> mapper.map(d, ProdutoDetalheDTO.class))
                    .collect(Collectors.toList());
            dto.setDetalhe(detalhes); // ✅ lista simples
        }

        return dto;
    }

    public Produto toEntity(ProdutoDTO dto) {
        Produto entity = mapper.map(dto, Produto.class);

        if (dto.getDetalhe() != null) {
            List<ProdutoDetalhe> detalhes = dto.getDetalhe().stream()
                    .map(d -> mapper.map(d, ProdutoDetalhe.class))
                    .collect(Collectors.toList());
            entity.setDetalhe(detalhes); // ✅ lista simples
        }

        return entity;
    }
}
