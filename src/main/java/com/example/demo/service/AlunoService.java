package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.AlunoDTO;
import com.example.demo.dto.AlunoPagamentoDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final AlunoPagamentoService alunoPagamentoService;

    public AlunoService(AlunoRepository repository,
                        AlunoMapper mapper,
                        ProfessorRepository professorRepository,
                        UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        EmailService emailService,
                        AlunoPagamentoService alunoPagamentoService) {
        this.repository = repository;
        this.mapper = mapper;
        this.professorRepository = professorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.alunoPagamentoService = alunoPagamentoService;
    }

    @Transactional
    public String create(AlunoDTO dto) {
        Aluno entity = mapper.toEntity(dto);
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        entity.setPrimeiroAcesso(true);
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        entity.setSenha(passwordEncoder.encode(senha));
        entity.setPerfil(Perfil.ALUNO);
        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            entity.setProfessor(professor);
        }

        Academia academia = obterAcademiaUsuario();
        if (academia == null) {
            throw new ApiException("Usuário precisa ter uma academia associada");
        }
        entity.setAcademia(academia);

        if (academia.getLimiteAlunos() != null) {
            long count = repository.countByAcademiaUuid(academia.getUuid());
            if (count >= academia.getLimiteAlunos()) {
                throw new ApiException("Limite de alunos da academia atingido");
            }
        }

        repository.save(entity);
        emailService.enviarSenha(entity.getEmail(), senha);
        return "Aluno criado com sucesso";
    }

    public Page<AlunoDTO> findAll(String nome, Pageable pageable) {
        Pageable sortedPageable = sortByNome(pageable);
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            if (nome != null && !nome.isBlank()) {
                return repository
                        .findByAcademiaUuidAndNomeContainingIgnoreCase(academia.getUuid(), nome, sortedPageable)
                        .map(this::mapToDto);
            }
            return repository.findByAcademiaUuid(academia.getUuid(), sortedPageable).map(this::mapToDto);
        }
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome, sortedPageable).map(this::mapToDto);
        }
        return repository.findAll(sortedPageable).map(this::mapToDto);
    }

    public AlunoDTO findByUuid(UUID uuid) {
        Aluno entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Aluno não encontrado"));
        Academia academia = obterAcademiaUsuario();
        if (academia != null && (entity.getAcademia() == null || !academia.getUuid().equals(entity.getAcademia().getUuid()))) {
            throw new ApiException("Acesso negado a aluno de outra academia");
        }
        return mapToDto(entity);
    }

    @Transactional
    public String update(UUID uuid, AlunoDTO dto) {
        Optional<Aluno> opt = repository.findById(uuid);
        if (opt.isEmpty()) {
            throw new ApiException("Aluno não encontrado");
        }
        Aluno entity = opt.get();

        Academia academia = obterAcademiaUsuario();
        if (academia != null) {
            if (entity.getAcademia() == null || !academia.getUuid().equals(entity.getAcademia().getUuid())) {
                throw new ApiException("Acesso negado a aluno de outra academia");
            }
            entity.setAcademia(academia);
        }

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
        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            entity.setProfessor(professor);
        } else {
            entity.setProfessor(null);
        }

        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuarioLogado != null && usuarioLogado.possuiPerfil(Perfil.ALUNO)
                && usuarioLogado.getUuid().equals(uuid)) {
            entity.setPrimeiroAcesso(false);
        }

        repository.save(entity);
        return "Aluno atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        Aluno entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Aluno não encontrado"));
        Academia academia = obterAcademiaUsuario();
        if (academia != null && (entity.getAcademia() == null || !academia.getUuid().equals(entity.getAcademia().getUuid()))) {
            throw new ApiException("Acesso negado a aluno de outra academia");
        }
        repository.delete(entity);
    }

    public Page<AlunoDTO> findAllFromLoggedProfessor(String nome, Pageable pageable) {
        Pageable sortedPageable = sortByNome(pageable);
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null || !usuario.possuiPerfil(Perfil.PROFESSOR)) {
            throw new ApiException("Usuário precisa ter perfil de professor");
        }
        Academia academia = obterAcademiaUsuario(usuario);
        if (nome != null && !nome.isBlank()) {
            return repository
                    .findByAcademiaUuidAndProfessorUuidAndNomeContainingIgnoreCase(academia.getUuid(), usuario.getUuid(), nome, sortedPageable)
                    .map(this::mapToDto);
        }
        return repository
                .findByAcademiaUuidAndProfessorUuid(academia.getUuid(), usuario.getUuid(), sortedPageable)
                .map(this::mapToDto);
    }

    public Page<AlunoDTO> findAllNotFromLoggedProfessor(String nome, Pageable pageable) {
        Pageable sortedPageable = sortByNome(pageable);
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null || !usuario.possuiPerfil(Perfil.PROFESSOR)) {
            throw new ApiException("Usuário precisa ter perfil de professor");
        }
        Academia academia = obterAcademiaUsuario(usuario);
        if (nome != null && !nome.isBlank()) {
            return repository
                    .findByAcademiaUuidAndProfessorUuidNotOrProfessorIsNullAndNomeContainingIgnoreCase(academia.getUuid(), usuario.getUuid(), nome, sortedPageable)
                    .map(this::mapToDto);
        }
        return repository
                .findByAcademiaUuidAndProfessorUuidNotOrProfessorIsNull(academia.getUuid(), usuario.getUuid(), sortedPageable)
                .map(this::mapToDto);
    }

    private Academia obterAcademiaUsuario() {
        return obterAcademiaUsuario(SecurityUtils.getUsuarioLogadoDetalhes());
    }

    private Academia obterAcademiaUsuario(UsuarioLogado usuario) {
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            Academia academia = usuarioEntity.getAcademia();
            if (academia == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            return academia;
        }
        return null;
    }

    private Pageable sortByNome(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("nome"));
    }

    private AlunoDTO mapToDto(Aluno aluno) {
        AlunoDTO dto = mapper.toDto(aluno);
        AlunoPagamentoDTO pagamento = alunoPagamentoService.buscarUltimoPagamento(aluno.getUuid());
        dto.setUltimoPagamento(pagamento);
        return dto;
    }
}
