package com.example.demo.service;

import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Exercicio;
import com.example.demo.entity.Musculo;
import com.example.demo.mapper.ExercicioMapper;
import com.example.demo.repository.ExercicioRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Usuario;
import com.example.demo.domain.enums.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class ExercicioService {
    private final ExercicioRepository repository;
    private final ExercicioMapper mapper;
    private final UsuarioRepository usuarioRepository;

    public ExercicioService(ExercicioRepository repository, ExercicioMapper mapper,
                            UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public String create(ExercicioDTO dto) {
        Exercicio entity = mapper.toEntity(dto);
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);

        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new IllegalArgumentException("Usuário precisa ter uma academia associada");
            }
            entity.setAcademia(academia);
        }

        repository.save(entity);
        return "Exercício criado com sucesso";
    }

    public Page<ExercicioDTO> find(String nome, Musculo musculo, Pageable pageable) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();

        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);

        if (isMaster) {
            return repository.findAllByNomeContainingIgnoreCaseAndMusculo(nome, musculo, pageable)
                    .map(mapper::toDto);
        } else {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();

            if (academia == null) {
                throw new IllegalArgumentException("Usuário precisa ter uma academia associada");
            }

            return repository.findByAcademiaAndFilters(academia.getUuid(), nome, musculo, pageable)
                    .map(mapper::toDto);
        }
    }
}
