package com.example.demo.repository.aluno;

import com.example.demo.domain.model.saude.Alergia;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlergiaRepository extends BaseRepository<Alergia, Long> {

    List<Alergia> findByAluno_Uuid(UUID alunoId);
}
