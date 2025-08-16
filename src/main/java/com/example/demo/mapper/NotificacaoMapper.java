package com.example.demo.mapper;

import com.example.demo.dto.NotificacaoDTO;
import com.example.demo.entity.Notificacao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoMapper {
    private final ModelMapper mapper;

    public NotificacaoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public NotificacaoDTO toDto(Notificacao notificacao) {
        return mapper.map(notificacao, NotificacaoDTO.class);
    }
}
