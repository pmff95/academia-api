package com.example.demo.service;

import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Exercicio;
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
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
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

    public Page<ExercicioDTO> findAll(Pageable pageable) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        Page<Exercicio> page;

        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);

        if (isMaster) {
            page = repository.findByAcademiaIsNull(pageable);
        } else {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();

            if (academia == null) {
                throw new IllegalArgumentException("Usuário precisa ter uma academia associada");
            }

            page = repository.findByAcademiaUuid(academia.getUuid(), pageable);
        }

        return page.map(mapper::toDto);
    }
}
