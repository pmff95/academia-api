package com.example.demo.service;

import com.example.demo.dto.AlunoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoMapper;
import com.example.demo.repository.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlunoService {
    private final AlunoRepository repository;
    private final AlunoMapper mapper;

    public AlunoService(AlunoRepository repository, AlunoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public AlunoDTO create(AlunoDTO dto) {
        Aluno entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Page<AlunoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public AlunoDTO findById(Long id) {
        Aluno entity = repository.findById(id).orElseThrow(() -> new ApiException("Aluno não encontrado"));
        return mapper.toDto(entity);
    }

    public AlunoDTO update(Long id, AlunoDTO dto) {
        Optional<Aluno> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw new ApiException("Aluno não encontrado");
        }
        Aluno entity = opt.get();
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setTelefone(dto.getTelefone());
        entity.setEmail(dto.getEmail());
        entity.setEnderecoCompleto(dto.getEnderecoCompleto());
        entity.setDataMatricula(dto.getDataMatricula());
        entity.setStatus(dto.getStatus());
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
