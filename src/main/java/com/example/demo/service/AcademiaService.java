package com.example.demo.service;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Usuario;
import com.example.demo.mapper.AcademiaMapper;
import com.example.demo.repository.AcademiaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.util.SenhaUtil;
import com.example.demo.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.UUID;

@Service
@Slf4j
public class AcademiaService {
    private final AcademiaRepository repository;
    private final AcademiaMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;

    public AcademiaService(AcademiaRepository repository, AcademiaMapper mapper, PasswordEncoder passwordEncoder,
                           EmailService emailService, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public String create(AcademiaDTO dto) { 
        try {
            Academia entity = mapper.toEntity(dto);
            Usuario admin = entity.getAdmin();
          
            validarAdministrador(admin);
          
            String senha = SenhaUtil.gerarSenhaNumerica(6);
            admin.setSenha(passwordEncoder.encode(senha));
            admin.setPerfil(Perfil.ADMIN);
            admin.setAcademia(entity);
            repository.save(entity);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        emailService.enviarSenha(admin.getEmail(), senha, entity.getUuid().toString());
                    } catch (Exception e) {
                        log.error("Erro ao enviar email", e);
                    }
                }
            });
            return "Academia criada com sucesso";
        } catch (Exception e) {
            log.error("Erro ao criar academia", e);
            return "Erro ao criar academia";
        }
    }

    private void validarAdministrador(Usuario admin) {
        if (admin == null || admin.getCpf() == null || admin.getEmail() == null || admin.getTelefone() == null) {
            throw new ApiException("Dados do administrador são obrigatórios");
        }
        if (usuarioRepository.findByCpfOrEmailOrTelefone(admin.getCpf(), admin.getEmail(), admin.getTelefone()).isPresent()) {
            throw new ApiException("Administrador já cadastrado");
        }
    }

    public Page<AcademiaDTO> findAll(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable).map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public AcademiaDTO findByUuid(UUID uuid) {
        Academia entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Academia não encontrada"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, AcademiaDTO dto) {
        Academia entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Academia não encontrada"));
        entity.setNome(dto.getNome());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        entity.setCep(dto.getCep());
        entity.setNumero(dto.getNumero());
        entity.setLogradouro(dto.getLogradouro());
        entity.setBairro(dto.getBairro());
        entity.setTelefone(dto.getTelefone());

        if (dto.getAdmin() != null && entity.getAdmin() != null) {
            entity.getAdmin().setNome(dto.getAdmin().getNome());
            entity.getAdmin().setCpf(dto.getAdmin().getCpf());
            entity.getAdmin().setDataNascimento(dto.getAdmin().getDataNascimento());
            entity.getAdmin().setTelefone(dto.getAdmin().getTelefone());
            entity.getAdmin().setTelefoneSecundario(dto.getAdmin().getTelefoneSecundario());
            entity.getAdmin().setEmail(dto.getAdmin().getEmail());
            entity.getAdmin().setNumero(dto.getAdmin().getNumero());
            entity.getAdmin().setCep(dto.getAdmin().getCep());
            entity.getAdmin().setLogradouro(dto.getAdmin().getLogradouro());
            entity.getAdmin().setUf(dto.getAdmin().getUf());
            entity.getAdmin().setCidade(dto.getAdmin().getCidade());
        }

        repository.save(entity);
        return "Academia atualizada";
    }
}
