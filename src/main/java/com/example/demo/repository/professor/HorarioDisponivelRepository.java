package com.example.demo.repository.professor;

import com.example.demo.domain.model.professor.HorarioDisponivel;
import com.example.demo.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface HorarioDisponivelRepository extends BaseRepository<HorarioDisponivel, Long> {

    <T> List<T> findAllByProfessorUuid(UUID professorUuid, Class<T> projection);
}
