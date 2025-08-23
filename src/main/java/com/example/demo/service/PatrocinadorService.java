package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.util.SenhaUtil;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.dto.PatrocinadorDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Patrocinador;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.PatrocinadorMapper;
import com.example.demo.repository.PatrocinadorRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
public class PatrocinadorService {
    private final PatrocinadorRepository repository;
    private final PatrocinadorMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PatrocinadorService(PatrocinadorRepository repository, PatrocinadorMapper mapper,
                               UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                               EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public String create(PatrocinadorDTO dto) {
        Patrocinador entity = mapper.toEntity(dto);
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        entity.setSenha(passwordEncoder.encode(senha));
        entity.setPerfil(Perfil.PATROCINADOR);
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
        return "Patrocinador criado com sucesso";
    }

    public Page<PatrocinadorDTO> findAll(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable).map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public PatrocinadorDTO findByUuid(UUID uuid) {
        Patrocinador entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Patrocinador não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, PatrocinadorDTO dto) {
        Patrocinador entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Patrocinador não encontrado"));
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
        repository.save(entity);
        return "Patrocinador atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}

