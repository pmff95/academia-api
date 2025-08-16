package com.example.demo.repository;

import com.example.demo.entity.FichaTreino;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FichaTreinoRepository extends JpaRepository<FichaTreino, UUID> {
    List<FichaTreino> findByAluno_Uuid(UUID alunoUuid);

    @EntityGraph(attributePaths = {"categorias"})
    Optional<FichaTreino> findByUuid(UUID uuid);
}
