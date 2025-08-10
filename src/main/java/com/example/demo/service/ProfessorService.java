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

    public ProfessorDTO findById(Long id) {
        Professor entity = repository.findById(id).orElseThrow(() -> new ApiException("Professor não encontrado"));
        return mapper.toDto(entity);
    }

    public String update(Long id, ProfessorDTO dto) {
        Professor entity = repository.findById(id)
                .orElseThrow(() -> new ApiException("Professor não encontrado"));
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setTelefone(dto.getTelefone());
        entity.setEmail(dto.getEmail());
        entity.setEnderecoCompleto(dto.getEnderecoCompleto());
        repository.save(entity);
        return "Professor atualizado";
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
