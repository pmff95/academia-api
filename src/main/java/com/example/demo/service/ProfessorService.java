package com.example.demo.service;

import com.example.demo.dto.ProfessorDTO;
import com.example.demo.entity.Professor;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.ProfessorMapper;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.util.SenhaUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Service
public class ProfessorService {
    private final ProfessorRepository repository;
    private final ProfessorMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ProfessorService(ProfessorRepository repository, ProfessorMapper mapper, PasswordEncoder passwordEncoder,
                            EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String create(ProfessorDTO dto) {
        Professor entity = mapper.toEntity(dto);
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setUf(dto.getUf());
        entity.setCidade(dto.getCidade());
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        entity.setSenha(passwordEncoder.encode(senha));
        entity.setPerfil(Perfil.PROFESSOR);
        repository.save(entity);
        emailService.enviarSenha(entity.getEmail(), senha);
        return "Professor criado com sucesso";
    }

    public Page<ProfessorDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public ProfessorDTO findByUuid(UUID uuid) {
        Professor entity = repository.findById(uuid).orElseThrow(() -> new ApiException("Professor não encontrado"));
        return mapper.toDto(entity);
    }

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
        repository.save(entity);
        return "Professor atualizado";
    }

    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}
