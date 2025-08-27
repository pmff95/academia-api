package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.ProfessorDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.ProfessorMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.util.SenhaUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProfessorService {
    private final ProfessorRepository repository;
    private final ProfessorMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ProfessorService(ProfessorRepository repository, ProfessorMapper mapper, UsuarioRepository usuarioRepository,
                            AlunoRepository alunoRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository = alunoRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public String create(ProfessorDTO dto) {
        Professor entity = mapper.toEntity(dto);
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        entity.setCref(dto.getCref());
        entity.setCrefValidade(dto.getCrefValidade());
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        entity.setSenha(passwordEncoder.encode(senha));
        entity.setPerfil(Perfil.PROFESSOR);
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            entity.setAcademia(academia);
        }

        repository.save(entity);
        emailService.enviarSenha(entity.getEmail(), senha);
        return "Professor criado com sucesso";
    }

    public Page<ProfessorDTO> findAll(String nome, UUID alunoUuid, Boolean possuiCref, Pageable pageable) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (alunoUuid != null) {
            Aluno aluno = alunoRepository.findById(alunoUuid)
                    .orElseThrow(() -> new ApiException("Aluno não encontrado"));
            if (usuario != null && !isMaster) {
                Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                        .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
                Academia academia = usuarioEntity.getAcademia();
                if (academia == null || aluno.getAcademia() == null
                        || !academia.getUuid().equals(aluno.getAcademia().getUuid())) {
                    throw new ApiException("Aluno não pertence à sua academia");
                }
            }
            Professor professor = aluno.getProfessor();
            if (professor == null) {
                return Page.<Professor>empty(pageable).map(mapper::toDto);
            }
            if (possuiCref != null) {
                if (possuiCref && professor.getCref() == null) {
                    return Page.<Professor>empty(pageable).map(mapper::toDto);
                }
                if (!possuiCref && professor.getCref() != null) {
                    return Page.<Professor>empty(pageable).map(mapper::toDto);
                }
            }
            return new PageImpl<>(List.of(mapper.toDto(professor)), pageable, 1);
        }
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            if (nome != null && !nome.isBlank()) {
                if (possuiCref != null) {
                    return (possuiCref
                            ? repository.findByAcademiaUuidAndNomeContainingIgnoreCaseAndCrefIsNotNull(academia.getUuid(), nome, pageable)
                            : repository.findByAcademiaUuidAndNomeContainingIgnoreCaseAndCrefIsNull(academia.getUuid(), nome, pageable))
                            .map(mapper::toDto);
                }
                return repository
                        .findByAcademiaUuidAndNomeContainingIgnoreCase(academia.getUuid(), nome, pageable)
                        .map(mapper::toDto);
            }
            if (possuiCref != null) {
                return (possuiCref
                        ? repository.findByAcademiaUuidAndCrefIsNotNull(academia.getUuid(), pageable)
                        : repository.findByAcademiaUuidAndCrefIsNull(academia.getUuid(), pageable))
                        .map(mapper::toDto);
            }
            return repository.findByAcademiaUuid(academia.getUuid(), pageable).map(mapper::toDto);
        }
        if (nome != null && !nome.isBlank()) {
            if (possuiCref != null) {
                return (possuiCref
                        ? repository.findByNomeContainingIgnoreCaseAndCrefIsNotNull(nome, pageable)
                        : repository.findByNomeContainingIgnoreCaseAndCrefIsNull(nome, pageable))
                        .map(mapper::toDto);
            }
            return repository.findByNomeContainingIgnoreCase(nome, pageable).map(mapper::toDto);
        }
        if (possuiCref != null) {
            return (possuiCref
                    ? repository.findByCrefIsNotNull(pageable)
                    : repository.findByCrefIsNull(pageable))
                    .map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public ProfessorDTO findByUuid(UUID uuid) {
        Professor entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Professor não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, ProfessorDTO dto) {
        Professor entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Professor não encontrado"));
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setTelefone(dto.getTelefone());
        entity.setTelefoneSecundario(dto.getTelefoneSecundario());
        entity.setEmail(dto.getEmail());
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        entity.setCref(dto.getCref());
        entity.setCrefValidade(dto.getCrefValidade());

        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            entity.setAcademia(academia);
        }

        repository.save(entity);
        return "Professor atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}
