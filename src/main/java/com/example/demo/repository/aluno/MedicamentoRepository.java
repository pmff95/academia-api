package com.example.demo.repository.aluno;

import com.example.demo.domain.model.saude.Medicamento;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicamentoRepository extends BaseRepository<Medicamento, Long> {

    List<Medicamento> findByAluno_Uuid(UUID alunoId);
}
