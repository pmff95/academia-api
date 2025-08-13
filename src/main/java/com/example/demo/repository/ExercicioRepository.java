package com.example.demo.repository;

import com.example.demo.entity.Exercicio;
import com.example.demo.entity.Musculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ExercicioRepository extends JpaRepository<Exercicio, UUID> {
    Page<Exercicio> findByAcademiaIsNull(Pageable pageable);

    @Query("""
                SELECT e 
                FROM Exercicio e
                WHERE e.academia IS NULL
                   OR e.academia.uuid = :academiaUuid
            """)
    Page<Exercicio> findByAcademiaIsNullOrAcademiaUuid(@Param("academiaUuid") UUID academiaUuid, Pageable pageable);

    Page<Exercicio> findByAcademiaUuid(UUID academiaUuid, Pageable pageable);

    @Query("""
            SELECT e
            FROM Exercicio e
            WHERE (:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
              AND (:musculo IS NULL OR e.musculo = :musculo)
        """)
    Page<Exercicio> findAllByNomeContainingIgnoreCaseAndMusculo(@Param("nome") String nome,
                                                                @Param("musculo") Musculo musculo,
                                                                Pageable pageable);

    @Query("""
            SELECT e
            FROM Exercicio e
            WHERE (e.academia IS NULL OR e.academia.uuid = :academiaUuid)
              AND (:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
              AND (:musculo IS NULL OR e.musculo = :musculo)
        """)
    Page<Exercicio> findByAcademiaAndFilters(@Param("academiaUuid") UUID academiaUuid,
                                             @Param("nome") String nome,
                                             @Param("musculo") Musculo musculo,
                                             Pageable pageable);
}
