package com.example.demo.repository;

import com.example.demo.entity.Exercicio;
import com.example.demo.entity.Musculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
                WHERE (:nome IS NULL OR LOWER(CAST(e.nome AS string))
                        LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
                  AND (:musculo IS NULL OR e.musculo = :musculo)
            """)
    Page<Exercicio> findAllPageableByNomeContainingIgnoreCaseAndMusculo(@Param("nome") String nome,
                                                                        @Param("musculo") Musculo musculo,
                                                                        Pageable pageable);

    @Query("""
                SELECT e
                FROM Exercicio e
                WHERE (e.academia IS NULL OR e.academia.uuid = :academiaUuid)
                  AND (:nome IS NULL OR LOWER(CAST(e.nome AS string))
                        LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
                  AND (:musculo IS NULL OR e.musculo = :musculo)
            """)
    Page<Exercicio> findByAcademiaAndFiltersPageable(@Param("academiaUuid") UUID academiaUuid,
                                                     @Param("nome") String nome,
                                                     @Param("musculo") Musculo musculo,
                                                     Pageable pageable);

    @Query("""
                SELECT e
                FROM Exercicio e
                WHERE (:nome IS NULL OR LOWER(CAST(e.nome AS string))
                        LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
                  AND (:musculos IS NULL OR e.musculo IN :musculos)
            """)
    List<Exercicio> findAllByNomeContainingIgnoreCaseAndMusculoIn(@Param("nome") String nome,
                                                                  @Param("musculos") List<Musculo> musculos);

    @Query("""
                SELECT e
                FROM Exercicio e
                WHERE (e.academia IS NULL OR e.academia.uuid = :academiaUuid)
                  AND (:nome IS NULL OR LOWER(CAST(e.nome AS string))
                        LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
                  AND (:musculos IS NULL OR e.musculo IN :musculos)
            """)
    List<Exercicio> findByAcademiaAndFilters(@Param("academiaUuid") UUID academiaUuid,
                                             @Param("nome") String nome,
                                             @Param("musculos") List<Musculo> musculos);
}
