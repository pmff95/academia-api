package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.AlunoDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.util.SenhaUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AlunoService {
    private final AlunoRepository repository;
    private final AlunoMapper mapper;
    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AlunoService(AlunoRepository repository, AlunoMapper mapper, ProfessorRepository professorRepository,
                        UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.professorRepository = professorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public String create(AlunoDTO dto) {
        Aluno entity = mapper.toEntity(dto);
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        entity.setSenha(passwordEncoder.encode(senha));
        entity.setPerfil(Perfil.ALUNO);
        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            entity.setProfessor(professor);
        }

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
        return "Aluno criado com sucesso";
    }

    public Page<AlunoDTO> findAll(Pageable pageable) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            return repository.findByAcademiaUuid(academia.getUuid(), pageable).map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public Page<AlunoDTO> findAllByEscola(Pageable pageable) {
//        return repository.findAllByescol(pageable).map(mapper::toDto);
        return null;
    }

    public AlunoDTO findByUuid(UUID uuid) {
        Aluno entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Aluno não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, AlunoDTO dto) {
        Optional<Aluno> opt = repository.findById(uuid);
        if (opt.isEmpty()) {
            throw new ApiException("Aluno não encontrado");
        }
        Aluno entity = opt.get();
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
        entity.setDataMatricula(dto.getDataMatricula());
        entity.setStatus(dto.getStatus());
        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            entity.setProfessor(professor);
        } else {
            entity.setProfessor(null);
        }

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
        return "Aluno atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}
