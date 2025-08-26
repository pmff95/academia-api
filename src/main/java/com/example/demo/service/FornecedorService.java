package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.util.SenhaUtil;
import com.example.demo.dto.FornecedorDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Fornecedor;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.FornecedorMapper;
import com.example.demo.repository.FornecedorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.domain.enums.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;
    private final FornecedorMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public FornecedorService(FornecedorRepository repository, FornecedorMapper mapper,
                             UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                             EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public String create(FornecedorDTO dto) {
        Fornecedor entity = mapper.toEntity(dto);
        entity.setTipo(dto.getTipo());
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        entity.setSenha(passwordEncoder.encode(senha));
        entity.setPerfil(Perfil.FORNECEDOR);
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
        return "Fornecedor criado com sucesso";
    }

    public Page<FornecedorDTO> findAll(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable).map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public FornecedorDTO findByUuid(UUID uuid) {
        Fornecedor entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Fornecedor não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, FornecedorDTO dto) {
        Fornecedor entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Fornecedor não encontrado"));
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
        entity.setTipo(dto.getTipo());
        repository.save(entity);
        return "Fornecedor atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}

