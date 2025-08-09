package com.example.demo.repository.professor;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.dto.projection.ProfessorDisciplinaView;
import com.example.demo.repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends BaseRepository<Professor, Long> {

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projectionClass);

    boolean existsByEmailAndEscolaUuid(String email, UUID escolaUuid);

    Optional<Professor> findByUuid(UUID uuid);

    boolean existsByCpfAndEscolaUuid(String cpf, UUID escolaUuid);


    boolean existsByCpfAndEscolaUuidAndUuidNot(String cpf, UUID escolaUuid, UUID uuid);

    boolean existsByEmailAndEscolaUuidAndUuidNot(String email, UUID escolaUuid, UUID uuid);

    /**
     * Returns all professors for the given status with their disciplines.
     */
    List<ProfessorDisciplinaView> findAllByStatus(Status status);

}
