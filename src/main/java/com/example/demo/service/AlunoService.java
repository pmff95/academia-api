package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Professor;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AlunoService {
    private final AlunoRepository repository;
    private final AlunoMapper mapper;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository repository, AlunoMapper mapper, ProfessorRepository professorRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String create(AlunoDTO dto) {
        Aluno entity = mapper.toEntity(dto);
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        if (dto.getProfessorId() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorId())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            entity.setProfessor(professor);
        }
        repository.save(entity);
        return "Aluno criado com sucesso";
    }

    public Page<AlunoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public AlunoDTO findById(Long id) {
        Aluno entity = repository.findById(id).orElseThrow(() -> new ApiException("Aluno não encontrado"));
        return mapper.toDto(entity);
    }

    public String update(Long id, AlunoDTO dto) {
        Optional<Aluno> opt = repository.findById(id);
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
        if (dto.getSenha() != null) {
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        entity.setDataMatricula(dto.getDataMatricula());
        entity.setStatus(dto.getStatus());
        if (dto.getProfessorId() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorId())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            entity.setProfessor(professor);
        } else {
            entity.setProfessor(null);
        }
        repository.save(entity);
        return "Aluno atualizado";
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
