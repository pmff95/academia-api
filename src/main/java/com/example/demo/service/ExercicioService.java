package com.example.demo.service;

import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Exercicio;
import com.example.demo.entity.Musculo;
import com.example.demo.entity.Maquina;
import com.example.demo.mapper.ExercicioMapper;
import com.example.demo.repository.ExercicioRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.MaquinaRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Usuario;
import com.example.demo.domain.enums.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class ExercicioService {
    private final ExercicioRepository repository;
    private final ExercicioMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final MaquinaRepository maquinaRepository;

    public ExercicioService(ExercicioRepository repository, ExercicioMapper mapper,
                            UsuarioRepository usuarioRepository, MaquinaRepository maquinaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.maquinaRepository = maquinaRepository;
    }

    @Transactional
    public String create(ExercicioDTO dto) {
        Exercicio entity = mapper.toEntity(dto);
        if (dto.getMaquinaUuid() != null) {
            Maquina maquina = maquinaRepository.findById(dto.getMaquinaUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Máquina não encontrada"));
            entity.setMaquina(maquina);
        }
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
            return repository.findAllPageableByNomeContainingIgnoreCaseAndMusculo(nome, musculo, pageable)
                    .map(mapper::toDto);
        } else {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();

            if (academia == null) {
                throw new IllegalArgumentException("Usuário precisa ter uma academia associada");
            }

            return repository.findByAcademiaAndFiltersPageable(academia.getUuid(), nome, musculo, pageable)
                    .map(mapper::toDto);
        }
    }

    public List<ExercicioDTO> buscarTodos(String nome, List<Musculo> musculos) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);

        List<Musculo> filtro = (musculos == null || musculos.isEmpty()) ? null : musculos;

        if (isMaster) {
            return repository.findAllByNomeContainingIgnoreCaseAndMusculoIn(nome, filtro)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
        } else {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();

            if (academia == null) {
                throw new IllegalArgumentException("Usuário precisa ter uma academia associada");
            }

            return repository.findByAcademiaAndFilters(academia.getUuid(), nome, filtro)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
        }
    }

}
